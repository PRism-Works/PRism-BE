package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.config.OpenAIConfig;
import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.model.EmailTemplate;
import com.prismworks.prism.domain.email.service.EmailSendService;
import com.prismworks.prism.domain.peerreview.converter.PeerReviewResponseConverter;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.dto.ShortAnswerResponse;
import com.prismworks.prism.domain.peerreview.exception.PeerReviewException;
import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.peerreview.model.PrismData;
import com.prismworks.prism.domain.project.dto.ProjectPeerReviewEmailInfoDto;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.*;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Repository
public class PeerReviewService {

    private final PeerReviewResponseHistoryService peerReviewResponseHistoryService;
    private final PeerReviewResultService peerReviewResultService;
    private final PeerReviewLinkCodeService peerReviewLinkCodeService;
    private final ProjectService projectService;
    private final EmailSendService emailSendService;

    private final ChatClient chatClient;

    private final PeerReviewResponseConverter peerReviewResponseConverter;

    public void sendPeerReviewLinkEmail(Integer projectId, String ownerEmail) {
        ProjectPeerReviewEmailInfoDto projectInfo = projectService.getProjectPeerReviewEmailInfo(projectId, ownerEmail);
        List<String> notReviewingMemberEmails = projectInfo.getNotReviewingMemberEmails();

        List<PeerReviewLinkCode> linkCodes =
                peerReviewLinkCodeService.createLinkCode(projectId, notReviewingMemberEmails);

        Map<String, Object> emailTemplateVariables = new HashMap<>();
        emailTemplateVariables.put("projectName", projectInfo.getProjectName());
        emailTemplateVariables.put("ownerName", projectInfo.getOwnerName());
        for(PeerReviewLinkCode linkCode : linkCodes) {
            emailTemplateVariables.put("code", linkCode.getCode());

            EmailSendRequest emailSendRequest = EmailSendRequest.builder()
                    .toEmails(List.of(linkCode.getReviewerEmail()))
                    .template(EmailTemplate.PEER_REVIEW_FORM)
                    .templateVariables(emailTemplateVariables)
                    .build();

            emailSendService.sendEmail(emailSendRequest);
        }
    }

    public ReviewLinkInfoResponse getReviewLinkInfo(String code) {
        PeerReviewLinkCode linkCode = peerReviewLinkCodeService.getLinkCode(code);
        Integer projectId = linkCode.getProjectId();
        String reviewerEmail = linkCode.getReviewerEmail();

        List<ProjectUserJoin> allMemberInProject = projectService.getAllMemberInProject(projectId);
        if(allMemberInProject.isEmpty()) {
            throw PeerReviewException.REVIEWEE_NOT_EXIST;
        }

        List<String> revieweeEmails = new ArrayList<>();
        for(ProjectUserJoin member : allMemberInProject) {
            String email = member.getEmail();
            if(reviewerEmail.equals(email)) {
                if(member.isPeerReviewDone()) {
                    throw PeerReviewException.ALREADY_FINISH_REVIEW;
                }
            } else {
                revieweeEmails.add(email);
            }
        }

        return PeerReviewDto.ReviewLinkInfoResponse.builder()
                .revieweeEmails(revieweeEmails)
                .projectId(projectId)
                .build();
    }

    public void createPeerReviewResponseHistory(Integer projectId,
                                                CreatePeerReviewResponseRequest request)
    {
        // todo: reviewerEmail이 projectMember인지 검증
        // todo: revieweeEmail이 projectMember인지 검증
        List<PeerReviewResponseHistory> histories = peerReviewResponseConverter.toPeerReviewResponseHistories(projectId, request);
        peerReviewResponseHistoryService.saveAllHistories(histories);
    }

    public void getProjectPrismData(Integer projectId) {
        List<PeerReviewResponseHistory> histories = peerReviewResponseHistoryService.getAllHistoriesByProject(projectId);
        List<PrismData> prismData = this.generatePrismData(histories);
    }

    public void getTotalPrismData() {

    }

    private List<PrismData> generatePrismData(List<PeerReviewResponseHistory> histories) {
        List<PrismData> prismDataList = new ArrayList<>();

        Map<String, List<PeerReviewResponseHistory>> groupingByReviewee =
                histories.stream().collect(groupingBy(PeerReviewResponseHistory::getReviewerEmail));
        for (Map.Entry<String, List<PeerReviewResponseHistory>> entry : groupingByReviewee.entrySet()) {
            String revieweeEmail = entry.getKey();
            List<PeerReviewResponseHistory> reviews = entry.getValue();
            int reviewerCount = reviews.size();

            PrismData prismData = new PrismData(revieweeEmail);

            List<ShortAnswerResponse> strengthFeedbacks = new ArrayList<>();
            for(PeerReviewResponseHistory review: reviews) {
                prismData.accumulateScore(review.getCommunicationScore(), review.getInitiativeScore(),
                        review.getProblemSolvingAbilityScore(), review.getResponsibilityScore(), review.getTeamworkScore());

                ShortAnswerResponse strengthFeedback =
                        peerReviewResponseConverter.toShortAnswerResponse(review.getStrengthFeedback());
                strengthFeedbacks.add(strengthFeedback);
            }
            prismData.averageScore(reviewerCount);

            PrismData.PrismSummaryData prismSummaryData = this.summaryReview(strengthFeedbacks);
            prismData.setReportData(prismSummaryData);

            prismDataList.add(prismData);
        }

        return prismDataList;
    }

    // todo: summary
    private PrismData.PrismSummaryData summaryReview(List<ShortAnswerResponse> strengthFeedback) {
        List<String> keywords = new ArrayList<>();
        String reviewSummary = "";

        return PrismData.PrismSummaryData.builder()
                .keywords(keywords)
                .reviewSummary(reviewSummary)
                .build();
    }
}

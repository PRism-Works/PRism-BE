package com.prismworks.prism.domain.peerreview.service;

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
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.*;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PeerReviewService {
    private static final Logger logger = LoggerFactory.getLogger(PeerReviewService.class);
    private final PeerReviewResponseHistoryService peerReviewResponseHistoryService;
    private final PeerReviewLinkCodeService peerReviewLinkCodeService;
    private final ProjectService projectService;
    private final UserService userService;
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

        List<RevieweeInfo> revieweeInfoList = new ArrayList<>();
        for(ProjectUserJoin member : allMemberInProject) {
            String revieweeEmail = member.getEmail();
            String revieweeName = member.getName();
            if(reviewerEmail.equals(revieweeEmail)) {
                if(member.isPeerReviewDone()) {
                    throw PeerReviewException.ALREADY_FINISH_REVIEW;
                }
            } else {
                revieweeInfoList.add(
                        RevieweeInfo.builder()
                                .revieweeName(revieweeName)
                                .revieweeEmail(revieweeEmail)
                                .build()
                );
            }
        }

        return PeerReviewDto.ReviewLinkInfoResponse.builder()
                .revieweeInfoList(revieweeInfoList)
                .projectId(projectId)
                .reviewerEmail(reviewerEmail)
                .build();
    }

    public void createPeerReviewResponseHistory(Integer projectId,
                                                CreatePeerReviewResponseRequest request)
    {
        // todo: reviewerEmail이 projectMember인지 검증
        // todo: revieweeEmail이 projectMember인지 검증
        List<PeerReviewResponseHistory> histories = peerReviewResponseConverter.toPeerReviewResponseHistories(projectId, request);
        projectService.memberDonePeerReview(projectId, request.getReviewerEmail());
        peerReviewResponseHistoryService.saveAllHistories(histories);
    }

    public List<PrismData> getNewEachPrismData(Integer projectId) {
        List<PeerReviewResponseHistory> histories = peerReviewResponseHistoryService.getAllHistoriesByProject(projectId);
        return this.generateEachPrismData(histories);
    }

    private List<PrismData> generateEachPrismData(List<PeerReviewResponseHistory> histories) {
        List<PrismData> prismDataList = new ArrayList<>();

        Map<String, List<PeerReviewResponseHistory>> groupingByReviewee =
                histories.stream().collect(groupingBy(PeerReviewResponseHistory::getRevieweeEmail));
        for (Map.Entry<String, List<PeerReviewResponseHistory>> entry : groupingByReviewee.entrySet()) {
            String revieweeEmail = entry.getKey();
            String revieweeUserId = null;
            Optional<Users> userOptional = userService.findUserByEmail(revieweeEmail);
            if(userOptional.isPresent()) {
                revieweeUserId = userOptional.get().getUserId();
            }

            List<PeerReviewResponseHistory> reviews = entry.getValue();
            int reviewerCount = reviews.size();

            PrismData prismData = new PrismData(revieweeEmail, revieweeUserId);

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
        List<String> keywords       = new ArrayList<>(); // 키워드
        String reviewSummary        = "";                // 팀원평가요약

        // todo: chatClient summary
        keywords            = getKeywordsFromLLM(strengthFeedback);
        reviewSummary       = getreviewSummaryFromLLM(strengthFeedback);

        return PrismData.PrismSummaryData.builder()
                .keywords(keywords)
                .reviewSummary(reviewSummary)
                .build();
    }

    private String callLLM(String message) {
        // 현재 시각을 YYYYMMDDHHmmss 형식으로 기록
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNow = now.format(formatter);

        // message와 시각을 로그로 기록
        logger.info("========================================");
        logger.info("Request received at: {}\n", formattedNow);
        logger.info("Input message: {}\n", message);

        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
        String answer = chatResponse.getResult().getOutput().getContent();

        // answer를 로그로 기록
        logger.info("Generated answer: {}\n", answer);

        return answer;
    }

    private List<String> getKeywordsFromLLM(List<ShortAnswerResponse> strengthFeedback) {
        StringBuilder prompt = new StringBuilder("당신은 특정 인물에 대한 주변 인물들의 평가를 기반으로 그 사람을 대표하는 키워드를 뽑아주는 전문가입니다.\n" +
                "아래 특정 인물에 대한 키워드들을 가지고 공통으로 출현하거나 비슷하게 출현하는 빈도가 높은 단어들을\n" +
                "빈도가 높은 순서대로 통합해서 추출해주세요.\n" +
                "주의 사항이 있습니다.\n" +
                "예시처럼 반드시 추출된 키워드들의 처음과 마지막에 k를 붙여 주세요.\n" +
                "또한, k부터 k까지의 단어 말고 다른 단어를 사용하지 마세요.\n" +
                "k키워드1,키워드2,키워드3k\n" +
                "평가데이터\n");

        for (int i = 0; i < strengthFeedback.size(); i++) {
            prompt.append(i + 1).append(". ").append(strengthFeedback.get(i).getDescription()).append("\n");
        }

        String answer               = callLLM(prompt.toString());
        String keywordSection       = answer.substring(answer.indexOf('k') + 1, answer.lastIndexOf('k'));

        List<String> keywordsList   = new ArrayList<>(Arrays.asList(keywordSection.split(",")));

        for (int i = 0; i < keywordsList.size(); i++) {
            keywordsList.set(i, keywordsList.get(i).trim());
        }
        logger.info("반환되는 keywordsList는 : " + keywordsList);
        logger.info("========================================");
        return keywordsList;
    }
    private String getreviewSummaryFromLLM(List<ShortAnswerResponse> strengthFeedback) {
        StringBuilder prompt = new StringBuilder("당신은 특정 인물에 대한 주변 인물들의 평가를 기반으로 그 사람에 대한 두 줄 요약글을 뽑아주는 전문가입니다.\n" +
                "아래 특정 인물에 대한 평가들을 가지고 공통으로 출현하거나 비슷하게 출현하는 빈도가 높은 단어들을\n" +
                "사용해서 두 줄 요약글을 작성해주세요.\n" +
                "주의 사항이 있습니다.\n" +
                "예시처럼 반드시 추출된 두 줄 요약글의 처음과 마지막에 k를 붙여 주세요.\n" +
                "또한, k부터 k까지의 단어 말고 다른 단어를 사용하지 마세요.\n" +
                "k모든 인물들의 평가를 두 줄로 요약한 글k\n" +
                "평가데이터\n");

        for (int i = 0; i < strengthFeedback.size(); i++) {
            prompt.append(i + 1).append(". ").append(strengthFeedback.get(i).getExample()).append("\n");
        }

        String answer   = callLLM(prompt.toString());
        answer          = answer.substring(answer.indexOf('k') + 1, answer.lastIndexOf('k'));
        answer          = answer.replace("k","");

        logger.info("반환되는 reviewSummary는 : " + answer);
        logger.info("========================================");
        return answer;
    }
}

package com.prismworks.prism.domain.peerreview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.dto.QuestionType;
import com.prismworks.prism.domain.peerreview.dto.ReviewResponse;
import com.prismworks.prism.domain.peerreview.exception.PeerReviewException;
import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponse;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.project.dto.ProjectPeerReviewEmailInfoDto;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.*;

@RequiredArgsConstructor
@Repository
public class PeerReviewService {

    private final PeerReviewResponseHistoryService peerReviewResponseHistoryService;
    private final PeerReviewResultService peerReviewResultService;
    private final PeerReviewLinkCodeService peerReviewLinkCodeService;
    private final ProjectService projectService; // todo: 분리

    private final ObjectMapper objectMapper;

    public List<PeerReviewLinkCode> createPeerReviewLinkCode(Integer projectId, String ownerEmail) {
        ProjectPeerReviewEmailInfoDto projectInfo = projectService.getProjectPeerReviewEmailInfo(projectId, ownerEmail);
        List<String> notReviewingMemberEmails = projectInfo.getNotReviewingMemberEmails();

        return peerReviewLinkCodeService.createLinkCode(projectId, notReviewingMemberEmails);
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
        List<PeerReviewResponseHistory> histories = this.parsePeerReviewResponses(projectId, request);
//        Long l = projectService.countUserInProject(projectId);
//        Long reviewerCountInProject = peerReviewResponseHistoryService.getReviewerCountInProject(projectId);
        peerReviewResponseHistoryService.saveAllHistories(histories);
    }

    private List<PeerReviewResponseHistory> parsePeerReviewResponses(Integer projectId, CreatePeerReviewResponseRequest request) {
        String reviewerEmail = request.getReviewerEmail();
        Map<String, List<PeerReviewResponse>> map = responseGroupByReviewee(request.getResponses());

        List<PeerReviewResponseHistory> histories = new ArrayList<>();
        for (Map.Entry<String, List<PeerReviewResponse>> entry : map.entrySet()) {
            String revieweeEmail = entry.getKey();
            List<PeerReviewResponse> responseMeta = entry.getValue();

            String responseMetaJson;
            try {
                responseMetaJson = objectMapper.writeValueAsString(responseMeta);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e); // todo
            }

            PeerReviewResponseHistory reviewResponseHistory = PeerReviewResponseHistory.builder()
                    .projectId(projectId)
                    .reviewerEmail(reviewerEmail)
                    .revieweeEmail(revieweeEmail)
                    .reviewResponse(responseMetaJson)
                    .build();

            histories.add(reviewResponseHistory);
        }

        return histories;
    }

    private Map<String, List<PeerReviewResponse>> responseGroupByReviewee(List<PeerReviewResponseItem> responses) {
        Map<String, List<PeerReviewResponse>> map = new HashMap<>();

        for(PeerReviewResponseItem responseItem : responses) {
            QuestionType questionType = responseItem.getQuestionType();
            List<PeerReviewResponseDetailItem> responseDetails = responseItem.getResponseDetails();

            for (PeerReviewResponseDetailItem responseDetailItem : responseDetails) {
                String revieweeEmail = responseDetailItem.getRevieweeEmail();
                ReviewResponse response = questionType.convertResponse(responseDetailItem.getResponse(), objectMapper);

                PeerReviewResponse responseMeta = PeerReviewResponse.builder()
                        .questionOrder(responseItem.getQuestionOrder())
                        .questionType(questionType)
                        .questionCategory(responseItem.getQuestionCategory())
                        .reviewResponse(response)
                        .build();

                map.computeIfAbsent(revieweeEmail, k -> new ArrayList<>()).add(responseMeta);
            }
        }
        return map;
    }
}

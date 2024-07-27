package com.prismworks.prism.domain.peerreview.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prismworks.prism.domain.peerreview.dto.*;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.CreatePeerReviewResponseRequest;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponse;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResultInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.PeerReviewResponseDetailItem;
import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.PeerReviewResponseItem;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Component
public class PeerReviewResponseConverter {

    private final ObjectMapper objectMapper;

    public List<PeerReviewResponseHistory> toPeerReviewResponseHistories(Integer projectId,
                                                                         CreatePeerReviewResponseRequest request)
    {
        String reviewerEmail = request.getReviewerEmail();
        Map<String, List<PeerReviewResponse>> map = requestGroupByReviewee(request.getResponses());

        List<PeerReviewResponseHistory> histories = new ArrayList<>();
        for (Map.Entry<String, List<PeerReviewResponse>> entry : map.entrySet()) {
            String revieweeEmail = entry.getKey();
            List<PeerReviewResponse> responseMeta = entry.getValue();

            PeerReviewResultInfo peerReviewResultInfo = this.extractResult(responseMeta);

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
                    .communicationScore(peerReviewResultInfo.getCommunicationScore())
                    .initiativeScore(peerReviewResultInfo.getInitiativeScore())
                    .problemSolvingAbilityScore(peerReviewResultInfo.getProblemSolvingAbilityScore())
                    .responsibilityScore(peerReviewResultInfo.getResponsibilityScore())
                    .teamworkScore(peerReviewResultInfo.getTeamworkScore())
                    .strengthFeedback(peerReviewResultInfo.getStrength())
                    .improvePointFeedback(peerReviewResultInfo.getImprovementPoint())
                    .reviewResponse(responseMetaJson)
                    .build();

            histories.add(reviewResponseHistory);
        }

        return histories;
    }

    public List<PeerReviewResponse> toPeerReviewResponses(String json) {
        List<PeerReviewResponse> responses = new ArrayList<>();
        try {
            responses = objectMapper.readValue(
                    json, objectMapper.getTypeFactory().constructCollectionType(List.class, PeerReviewResponse.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return responses;
    }

    private Map<String, List<PeerReviewResponse>> requestGroupByReviewee(List<PeerReviewResponseItem> responses) {
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

    @SneakyThrows
    private PeerReviewResultInfo extractResult(List<PeerReviewResponse> responses) {
        Map<QuestionCategory, List<PeerReviewResponse>> collect =
                responses.stream().collect(groupingBy(PeerReviewResponse::getQuestionCategory));

        String strength = "";
        String improvementPoint = "";
        float communicationScore = 0F;
        float initiativeScore = 0F;
        float problemSolvingAbilityScore = 0F;
        float responsibilityScore = 0F;
        float teamworkScore = 0F;

        for (Map.Entry<QuestionCategory, List<PeerReviewResponse>> responseEntry : collect.entrySet()) {
            QuestionCategory category = responseEntry.getKey();
            List<PeerReviewResponse> value = responseEntry.getValue();

            if(QuestionCategory.STRENGTH.equals(category)) {
                strength = objectMapper.writeValueAsString(((ShortAnswerResponse) (value.get(0).getReviewResponse())));
            } else if(QuestionCategory.IMPROVEMENT_POINT.equals(category)) {
                improvementPoint = objectMapper.writeValueAsString(((ShortAnswerResponse) (value.get(0).getReviewResponse())));
            } else {
                int sum = value.stream()
                        .filter(a -> !QuestionType.MULTIPLE_CHOICE_MEMBER.equals(a.getQuestionType())) // todo: 팀원 선택형 답변 결과 계산
                        .mapToInt(a -> ((SingleChoiceResponse) (a.getReviewResponse())).getScore())
                        .sum();

                float averageScore = sum / (float) value.size();
                switch (category) {
                    case COMMUNICATION -> communicationScore = averageScore;
                    case INITIATIVE ->  initiativeScore = averageScore;
                    case PROBLEM_SOLIVING -> problemSolvingAbilityScore = averageScore * 2; // todo: 팀원 선택형 답변 결과 계산
                    case RESPONSIBILITY -> responsibilityScore = averageScore;
                    case TEAMWORK -> teamworkScore = averageScore;
                }
            }
        }

        return PeerReviewResultInfo.builder()
                .communicationScore(communicationScore)
                .initiativeScore(initiativeScore)
                .problemSolvingAbilityScore(problemSolvingAbilityScore)
                .responsibilityScore(responsibilityScore)
                .teamworkScore(teamworkScore)
                .strength(strength)
                .improvementPoint(improvementPoint)
                .build();
    }
}

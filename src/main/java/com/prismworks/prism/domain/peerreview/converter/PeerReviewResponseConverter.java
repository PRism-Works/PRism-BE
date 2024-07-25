package com.prismworks.prism.domain.peerreview.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.CreatePeerReviewResponseRequest;
import com.prismworks.prism.domain.peerreview.dto.QuestionType;
import com.prismworks.prism.domain.peerreview.dto.ReviewResponse;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponse;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.*;

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
}

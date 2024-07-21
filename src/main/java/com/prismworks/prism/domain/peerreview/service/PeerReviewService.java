package com.prismworks.prism.domain.peerreview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.dto.QuestionCategory;
import com.prismworks.prism.domain.peerreview.dto.QuestionType;
import com.prismworks.prism.domain.peerreview.dto.ReviewResponse;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponse;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResponseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class PeerReviewService {

    private final PeerReviewResponseHistoryRepository peerReviewResponseHistoryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void createPeerReviewResponseHistory(PeerReviewDto.CreatePeerReviewResponseRequest request) throws JsonProcessingException {
        String reviewerEmail = request.getReviewerEmail();
        int projectId = request.getProjectId();
        Map<String, List<PeerReviewResponse>> map = responseGroupByReviewee(request.getResponses());

        List<PeerReviewResponseHistory> histories = new ArrayList<>();
        for (Map.Entry<String, List<PeerReviewResponse>> entry : map.entrySet()) {
            String revieweeEmail = entry.getKey();
            List<PeerReviewResponse> responseMeta = entry.getValue();

            String responseMetaJson = objectMapper.writeValueAsString(responseMeta);

            PeerReviewResponseHistory reviewResponseHistory = PeerReviewResponseHistory.builder()
                    .projectId(projectId)
                    .reviewerEmail(reviewerEmail)
                    .revieweeEmail(revieweeEmail)
                    .reviewResponse(responseMetaJson)
                    .build();

            histories.add(reviewResponseHistory);
        }

        peerReviewResponseHistoryRepository.saveAll(histories);
    }

    private Map<String, List<PeerReviewResponse>> responseGroupByReviewee(List<PeerReviewDto.PeerReviewResponseItem> responses) {
        Map<String, List<PeerReviewResponse>> map = new HashMap<>();

        for(PeerReviewDto.PeerReviewResponseItem responseItem : responses) {
            int questionOrder = responseItem.getQuestionOrder();
            QuestionType questionType = responseItem.getQuestionType();
            QuestionCategory questionCategory = responseItem.getQuestionCategory();
            List<PeerReviewDto.PeerReviewResponseDetailItem> responseDetails = responseItem.getResponseDetails();

            for (PeerReviewDto.PeerReviewResponseDetailItem responseDetailItem : responseDetails) {
                String revieweeEmail = responseDetailItem.getRevieweeEmail();
                ReviewResponse response = questionType.convertResponse(responseDetailItem.getResponse(), objectMapper);

                PeerReviewResponse responseMeta = PeerReviewResponse.builder()
                        .questionOrder(questionOrder)
                        .questionType(questionType)
                        .questionCategory(questionCategory)
                        .reviewResponse(response)
                        .build();

                if(map.containsKey(revieweeEmail)) {
                    map.get(revieweeEmail).add(responseMeta);
                } else {
                    map.put(revieweeEmail, new ArrayList<>(List.of(responseMeta)));
                }
            }
        }
        return map;
    }
}

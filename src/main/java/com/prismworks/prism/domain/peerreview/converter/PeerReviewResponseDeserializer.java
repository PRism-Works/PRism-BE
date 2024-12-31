package com.prismworks.prism.domain.peerreview.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prismworks.prism.domain.peerreview.dto.*;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PeerReviewResponseDeserializer extends JsonDeserializer<PeerReviewResponse> {

    private final ObjectMapper objectMapper;

    @Override
    public PeerReviewResponse deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode rootNode = objectMapper.readTree(p);

        int questionOrder = rootNode.get("questionOrder").asInt();
        QuestionCategory questionCategory = QuestionCategory.from(rootNode.get("questionCategory").asText());
        QuestionType questionType = QuestionType.from(rootNode.get("questionType").asText());

        JsonNode reviewResponseNode = rootNode.get("reviewResponse");

        ReviewResponse reviewResponse;
        switch (questionType) {
            case SINGLE_CHOICE:
                reviewResponse = objectMapper.treeToValue(reviewResponseNode, SingleChoiceResponse.class);
                break;
            case MULTIPLE_CHOICE_MEMBER:
                reviewResponse = objectMapper.treeToValue(reviewResponseNode, MultipleChoiceMemberResponse.class);
                break;
            case SHORT_ANSWER:
                reviewResponse = objectMapper.treeToValue(reviewResponseNode, ShortAnswerResponse.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown question type: " + questionType);
        }

        return PeerReviewResponse.builder()
                .questionOrder(questionOrder)
                .questionCategory(questionCategory)
                .questionType(questionType)
                .reviewResponse(reviewResponse)
                .build();
    }
}

package com.prismworks.prism.domain.peerreview.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prismworks.prism.domain.peerreview.converter.PeerReviewResponseDeserializer;
import com.prismworks.prism.domain.peerreview.dto.ReviewResponse;
import com.prismworks.prism.domain.peerreview.dto.QuestionCategory;
import com.prismworks.prism.domain.peerreview.dto.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = PeerReviewResponseDeserializer.class)
public class PeerReviewResponse {
    private int questionOrder;
    private QuestionType questionType;
    private QuestionCategory questionCategory;
    private ReviewResponse reviewResponse;
}

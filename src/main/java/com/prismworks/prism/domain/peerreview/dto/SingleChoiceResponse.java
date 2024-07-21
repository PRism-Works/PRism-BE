package com.prismworks.prism.domain.peerreview.dto;

import lombok.Data;

@Data
public class SingleChoiceResponse extends ReviewResponse {
    private int score;
}

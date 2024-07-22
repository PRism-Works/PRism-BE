package com.prismworks.prism.domain.peerreview.dto;

import lombok.Data;

@Data
public class ShortAnswerResponse extends ReviewResponse {
    private String description;
    private String example;
}

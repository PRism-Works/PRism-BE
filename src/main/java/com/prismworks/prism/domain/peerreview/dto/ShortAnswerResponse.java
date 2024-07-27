package com.prismworks.prism.domain.peerreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortAnswerResponse extends ReviewResponse {
    private String description;
    private String example;
}

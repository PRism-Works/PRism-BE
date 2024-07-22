package com.prismworks.prism.domain.peerreview.dto;

import lombok.Data;


@Data
public class MultipleChoiceMemberResponse extends ReviewResponse{
    private boolean choice;
}

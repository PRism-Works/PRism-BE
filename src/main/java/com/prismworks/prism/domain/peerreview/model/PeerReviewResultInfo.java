package com.prismworks.prism.domain.peerreview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PeerReviewResultInfo {
    private String strength = "";
    private String improvementPoint = "";
    private float communicationScore = 0F;
    private float initiativeScore = 0F;
    private float problemSolvingAbilityScore = 0F;
    private float responsibilityScore = 0F;
    private float teamworkScore = 0F;
}

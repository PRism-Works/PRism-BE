package com.prismworks.prism.domain.peerreview.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum QuestionCategory {
    RESPONSIBILITY("responsibility"), // 책임감
    INITIATIVE("initiative"), // 적극성
    PROBLEM_SOLIVING("problemSolving"), // 문제해결능력
    COMMUNICATION("communication"), // 의사소통
    TEAMWORK("teamwork"), // 협동심
    STRENGTH("strength"), // 강점
    IMPROVEMENT_POINT("improvementPoint"); // 보완점

    private final String value;

    QuestionCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static QuestionCategory from(String value) {
        return Arrays.stream(QuestionCategory.values())
                .filter(category -> category.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("QuestionCategory not match")); //todo: custom Exception
    }
}

package com.prismworks.prism.domain.post.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyMethod {
    KAKAO_TALK("kakaoTalk"),
    EMAIL("email"),
    GOOGLE_FORM("googleForm");

    private final String value;

    @JsonCreator
    public static ApplyMethod from(String value) {
        return Arrays.stream(ApplyMethod.values())
            .filter(applyMethod -> applyMethod.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("ApplicationMethod not match")); //todo: custom Exception
    }
}

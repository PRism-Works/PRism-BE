package com.prismworks.prism.domain.post.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static ApplyMethod from(String value) {
        return Arrays.stream(ApplyMethod.values())
            .filter(applyMethod -> applyMethod.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown applyMethod value " + value));
    }
}

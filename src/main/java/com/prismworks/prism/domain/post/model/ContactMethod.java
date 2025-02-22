package com.prismworks.prism.domain.post.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContactMethod {
    KAKAO_TALK("kakaoTalk"),
    EMAIL("email"),
    GOOGLE_FORM("googleForm");

    private final String value;

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static ContactMethod from(String value) {
        return Arrays.stream(ContactMethod.values())
            .filter(contactMethod -> contactMethod.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown contactMethod value " + value));
    }
}

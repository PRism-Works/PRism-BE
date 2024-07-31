package com.prismworks.prism.domain.peerreview.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;

public enum QuestionType {
    SINGLE_CHOICE("singleChoice"){
        @Override
        public ReviewResponse convertResponse(Object response, ObjectMapper objectMapper) {
            return objectMapper.convertValue(response, SingleChoiceResponse.class);
        }
    },
    MULTIPLE_CHOICE_MEMBER("multipleChoiceMember") {
        @Override
        public ReviewResponse convertResponse(Object response, ObjectMapper objectMapper) {
            return objectMapper.convertValue(response, MultipleChoiceMemberResponse.class);
        }
    },
    SHORT_ANSWER("shortAnswer") {
        @Override
        public ReviewResponse convertResponse(Object response, ObjectMapper objectMapper) {
            return objectMapper.convertValue(response, ShortAnswerResponse.class);
        }
    };

    public abstract ReviewResponse convertResponse(Object response, ObjectMapper objectMapper);

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static QuestionType from(String value) {
        return Arrays.stream(QuestionType.values())
                .filter(type -> type.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("QuestionType not match")); //todo: custom Exception
    }
}

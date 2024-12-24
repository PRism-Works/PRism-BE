package com.prismworks.prism.domain.post.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessMethod {
    ONLINE("online"),
    OFFLINE("offline"),
    ONLINE_AND_OFFLINE("onlineAndOffline");

    private final String value;

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static ProcessMethod from(String value) {
        return Arrays.stream(ProcessMethod.values())
            .filter(projectProcessMethod -> projectProcessMethod.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("ProcessMethod not match")); //todo: custom Exception
    }
}

package com.prismworks.prism.domain.post.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectProcessMethod {
    ONLINE("online"),
    OFFLINE("offline"),
    ONLINE_AND_OFFLINE("onlineAndOffline");

    private final String value;

    @JsonCreator
    public static ProjectProcessMethod from(String value) {
        return Arrays.stream(ProjectProcessMethod.values())
            .filter(projectProcessMethod -> projectProcessMethod.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("ProcessMethod not match")); //todo: custom Exception
    }
}

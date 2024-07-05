package com.prismworks.prism.domain.email.model;

import lombok.Getter;

@Getter
public enum AuthType {

    SIGNUP("SIGNUP"),
    RESET_PASSWORD("RESET_PASSWORD");

    private final String value;

    AuthType(String value) {
        this.value = value;
    }
}

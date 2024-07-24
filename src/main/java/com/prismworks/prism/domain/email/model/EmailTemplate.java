package com.prismworks.prism.domain.email.model;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    SIGNUP("signup", false, "[PRism]회원가입 인증"),
    RESET_PASSWORD("reset-password", false, "[PRism]비밀번호 찾기 인증"),
    PEER_REVIEW_FORM("peer-review-form", true, "[PRism]동료평가 링크");

    private final String templateName;
    private final boolean isFrontOriginRequired;
    private final String subject;

    EmailTemplate(String templateName, boolean isFrontOriginRequired, String subject) {
        this.templateName = templateName;
        this.isFrontOriginRequired = isFrontOriginRequired;
        this.subject = subject;
    }
}

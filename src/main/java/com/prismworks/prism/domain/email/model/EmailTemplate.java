package com.prismworks.prism.domain.email.model;

import lombok.Getter;

import java.util.Map;

@Getter
public enum EmailTemplate {
    SIGNUP("prism-emailVerification", false, "[PRism] 이메일 인증 번호 안내",
            Map.of("logo-image", "logo-image.png")),
    RESET_PASSWORD("prism-emailVerification", false, "[PRism] 이메일 인증 번호 안내",
            Map.of("logo-image", "logo-image.png")),
    PEER_REVIEW_FORM("prism-survey", true, "[PRism] 동료 평가 요청",
            Map.of("logo-image", "logo-image.png",
                    "prism-image", "prism-email.png"));

    private final String templateName;
    private final boolean isFrontOriginRequired;
    private final String subject;
    private final Map<String, String> images;

    EmailTemplate(String templateName, boolean isFrontOriginRequired, String subject, Map<String, String> images) {
        this.templateName = templateName;
        this.isFrontOriginRequired = isFrontOriginRequired;
        this.subject = subject;
        this.images = images;
    }
}

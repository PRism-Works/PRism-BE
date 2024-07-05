package com.prismworks.prism.domain.email.dto;

import com.prismworks.prism.domain.email.model.EmailSendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailSendResult {
    private EmailSendStatus status;
    private String failReason;

    public EmailSendResult(EmailSendStatus status) {
        this.status = status;
    }

    public static EmailSendResult createSendResult() {
        return new EmailSendResult(EmailSendStatus.SENT);
    }

    public static EmailSendResult createFailResult(String failReason) {
        return new EmailSendResult(EmailSendStatus.FAILED, failReason);
    }
}

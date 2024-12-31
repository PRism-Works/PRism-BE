package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.domain.email.dto.EmailSendRequest;

public interface EmailSendService {
    void sendEmail(EmailSendRequest sendRequest);

    void sendEmailAsync(EmailSendRequest sendRequest);
}

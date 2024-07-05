package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import jakarta.mail.MessagingException;

public interface EmailSendService {
    void sendEmail(EmailSendRequest sendRequest);
}

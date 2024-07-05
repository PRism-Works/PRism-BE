package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.dto.EmailSendResult;
import com.prismworks.prism.domain.email.model.EmailSendLog;
import com.prismworks.prism.domain.email.repository.EmailSendLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EmailSendLogService {
    private final EmailSendLogRepository emailSendLogRepository;

    @Transactional
    public void saveEmailSendLog(EmailSendRequest request, EmailSendResult result) {
        EmailSendLog emailSendLog = EmailSendLog.builder()
                .receiverEmail(request.getToEmail())
                .senderEmail(request.getFromEmail())
                .path(request.getTemplate())
                .status(result.getStatus())
                .failReason(result.getFailReason())
                .build();

        emailSendLogRepository.save(emailSendLog);
    }
}

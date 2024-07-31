package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.dto.EmailSendResult;
import com.prismworks.prism.domain.email.model.EmailSendLog;
import com.prismworks.prism.domain.email.repository.EmailSendLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmailSendLogService {
    private final EmailSendLogRepository emailSendLogRepository;

    @Transactional
    public void saveEmailSendLog(EmailSendRequest request, EmailSendResult result) {
        List<EmailSendLog> logs = new ArrayList<>();
        for (String toEmail : request.getToEmails()) {
            EmailSendLog emailSendLog = EmailSendLog.builder()
                    .receiverEmail(toEmail)
                    .senderEmail(request.getFromEmail())
                    .path(request.getTemplate())
                    .status(result.getStatus())
                    .failReason(result.getFailReason())
                    .build();

            logs.add(emailSendLog);
        }

        emailSendLogRepository.saveAll(logs);
    }
}

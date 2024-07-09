package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.dto.EmailSendResult;
import com.prismworks.prism.domain.email.exception.EmailErrorCode;
import com.prismworks.prism.domain.email.model.EmailTemplate;
import com.prismworks.prism.domain.email.util.EmailTemplateConverter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class SmtpEmailSendService implements EmailSendService{

    @Value("${service.sender-email}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;
    private final EmailTemplateConverter templateConverter;
    private final EmailSendLogService emailSendLogService;

    @Override
    public void sendEmail(EmailSendRequest sendRequest) { // todo: async
        MimeMessage mimeMessage = this.generateEmailMessage(sendRequest);
        try {
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            String exMessage = e.getMessage();
            String failReason = exMessage.length() > 255 ? exMessage.substring(0, 255) : exMessage;
            EmailSendResult sendResult = EmailSendResult.createFailResult(failReason);
            emailSendLogService.saveEmailSendLog(sendRequest, sendResult);
            throw new ApplicationException(exMessage, EmailErrorCode.EMAIL_SEND_FAILED);
        }

        emailSendLogService.saveEmailSendLog(sendRequest, EmailSendResult.createSendResult());
    }

    private String getMailContentFromTemplate(EmailTemplate template, Map<String, Object> variables) {
        return templateConverter.convertToStr(template, variables);
    }

    private MimeMessage generateEmailMessage(EmailSendRequest sendRequest) {
        EmailTemplate emailTemplate = sendRequest.getTemplate();
        String content = this.getMailContentFromTemplate(emailTemplate, sendRequest.getTemplateVariables());
        String from = sendRequest.getFromEmail();

        if(!StringUtils.hasText(from)) {
            from = senderEmail;
            sendRequest.setFromEmail(from);
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(sendRequest.getToEmail());
            mimeMessageHelper.setSubject(emailTemplate.getSubject());
            mimeMessageHelper.setText(content, true);
        } catch (MessagingException e) {
            throw new ApplicationException(EmailErrorCode.EMAIL_MESSAGE_CREATION_FAILED);
        }

        return mimeMessage;
    }
}
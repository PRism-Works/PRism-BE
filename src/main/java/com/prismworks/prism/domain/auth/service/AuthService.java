package com.prismworks.prism.domain.auth.service;

import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.domain.auth.dto.AuthDto;
import com.prismworks.prism.domain.auth.exception.AuthErrorCode;
import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.prismworks.prism.domain.email.model.EmailTemplate;
import com.prismworks.prism.domain.email.service.EmailAuthCodeService;
import com.prismworks.prism.domain.email.service.EmailSendService;
import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.User;
import com.prismworks.prism.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final EmailSendService emailSendService;
    private final EmailAuthCodeService emailAuthCodeService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if(!StringUtils.hasText(email)) {
            return false;
        }

        return userService.userExistByEmail(email);
    }

    public void sendAuthCode(AuthDto.SendCodeRequest dto) {
        EmailAuthCode emailAuthCode =
                emailAuthCodeService.createEmailAuthCode(dto.getEmail(), dto.getAuthType(), dto.getRequestAt());

        EmailSendRequest emailSendRequest = EmailSendRequest.builder() // todo: refactor
                .toEmail(emailAuthCode.getEmail())
                .template(EmailTemplate.valueOf(emailAuthCode.getAuthType().getValue()))
                .templateVariables(Map.of("code", emailAuthCode.getCode()))
                .build();

        emailSendService.sendEmail(emailSendRequest);
    }

    @Transactional
    public void verifyAuthCode(AuthDto.VerifyCodeRequest dto) {
        EmailAuthCode authCode = emailAuthCodeService
                .findByEmailAndCodeAndAuthType(dto.getEmail(), dto.getAuthCode(), dto.getAuthType())
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.CODE_NOT_MATCH));

        if(!authCode.isValid(dto.getRequestAt())) {
            throw new ApplicationException(AuthErrorCode.CODE_ALREADY_EXPIRED);
        }

        authCode.verified(LocalDateTime.now());
    }

    public AuthDto.SignupResponse signup(AuthDto.SignupRequest dto) {
        boolean emailVerified = emailAuthCodeService.isEmailVerified(dto.getEmail());
        if(!emailVerified) {
            throw new ApplicationException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }

        UserDto.Create userCreateDto = UserDto.Create.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        User user = userService.createUser(userCreateDto);

        return AuthDto.SignupResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}

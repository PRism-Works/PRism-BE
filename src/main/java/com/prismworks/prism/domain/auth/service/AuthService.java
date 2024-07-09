package com.prismworks.prism.domain.auth.service;

import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.domain.auth.exception.AuthErrorCode;
import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.prismworks.prism.domain.email.model.EmailTemplate;
import com.prismworks.prism.domain.email.service.EmailAuthCodeService;
import com.prismworks.prism.domain.email.service.EmailSendService;
import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.service.UserService;
import com.prismworks.prism.domain.auth.dto.JwtTokenDto;
import com.prismworks.prism.domain.auth.model.RefreshToken;
import com.prismworks.prism.domain.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.prismworks.prism.domain.auth.dto.AuthDto.*;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final EmailSendService emailSendService;
    private final EmailAuthCodeService emailAuthCodeService;
    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if(!StringUtils.hasText(email)) {
            return false;
        }

        return userService.userExistByEmail(email);
    }

    public void sendAuthCode(SendCodeRequest dto) {
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
    public void verifyAuthCode(VerifyCodeRequest dto) {
        EmailAuthCode authCode = emailAuthCodeService
                .findByEmailAndCodeAndAuthType(dto.getEmail(), dto.getAuthCode(), dto.getAuthType())
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.CODE_NOT_MATCH));

        if(!authCode.isValid(dto.getRequestAt())) {
            throw new ApplicationException(AuthErrorCode.CODE_ALREADY_EXPIRED);
        }

        authCode.verified(LocalDateTime.now());
    }

    public SignupResponse signup(SignupRequest dto) {
        boolean emailVerified = emailAuthCodeService.isEmailVerified(dto.getEmail());
        if(!emailVerified) {
            throw new ApplicationException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        UserDto.Create userCreateDto = UserDto.Create.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .encodedPassword(encodedPassword)
                .build();

        Users user = userService.createUser(userCreateDto);

        return SignupResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public TokenResponse login(LoginRequest dto) {
        Users user = userService.findByEmail(dto.getEmail());
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if(!matches) {
            throw new ApplicationException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(user.getUserId(), dto.getRequestAt());

        return TokenResponse.builder()
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }

    public TokenResponse reissueToken(RefreshTokenRequest dto) {
        LocalDateTime requestedDateTime = dto.getRequestAt();
        Date requestedDate  = Date
                .from(requestedDateTime.atZone(ZoneId.systemDefault())
                .toInstant());

        RefreshToken refreshToken = jwtTokenProvider.validateRefreshToken(dto.getRefreshToken(), requestedDateTime);
        String userId = refreshToken.getUserId();

        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(userId, requestedDate);

        return TokenResponse.builder()
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }
}

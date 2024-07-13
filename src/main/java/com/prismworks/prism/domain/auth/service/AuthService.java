package com.prismworks.prism.domain.auth.service;

import com.prismworks.prism.domain.auth.dto.JwtTokenDto;
import com.prismworks.prism.domain.auth.exception.AuthException;
import com.prismworks.prism.domain.auth.model.AuthToken;
import com.prismworks.prism.domain.auth.provider.JwtTokenProvider;
import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.prismworks.prism.domain.email.model.EmailTemplate;
import com.prismworks.prism.domain.email.service.EmailAuthCodeService;
import com.prismworks.prism.domain.email.service.EmailSendService;
import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.prismworks.prism.domain.auth.dto.AuthDto.*;
import static com.prismworks.prism.utils.DateUtil.fromLocalDateTime;
import static com.prismworks.prism.utils.DateUtil.toLocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final EmailSendService emailSendService;
    private final EmailAuthCodeService emailAuthCodeService;
    private final UserService userService;
    private final AuthTokenService authTokenService;
    private final AuthTokenBlackListService authTokenBlackListService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(() -> AuthException.CODE_NOT_MATCH);

        if(!authCode.isValid(dto.getRequestAt())) {
            throw AuthException.CODE_ALREADY_EXPIRED;
        }

        authCode.verified(LocalDateTime.now());
    }

    public SignupResponse signup(SignupRequest dto) {
        boolean emailVerified = emailAuthCodeService.isEmailVerified(dto.getEmail());
        if(!emailVerified) {
            throw AuthException.EMAIL_NOT_VERIFIED;
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        UserDto.CreateInfo userCreateInfo = UserDto.CreateInfo.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .encodedPassword(encodedPassword)
                .build();

        Users user = userService.createUser(userCreateInfo);

        return SignupResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .build();
    }

    public TokenResponse login(LoginRequest dto) {
        Optional<Users> userOptional = userService.findUserByEmail(dto.getEmail());
        if(userOptional.isEmpty()) {
            throw AuthException.EMAIL_NOT_REGISTERED;
        }

        Users user = userOptional.get();
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if(!matches) {
            throw AuthException.PASSWORD_NOT_MATCH;
        }

        JwtTokenDto jwtTokenDto = this.generateToken(user.getUserId(), dto.getRequestAt());

        return TokenResponse.builder()
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }

    public TokenResponse reissueToken(RefreshTokenRequest dto) {
        LocalDateTime requestAt = dto.getRequestAt();
        AuthToken authToken = authTokenService.findByToken(dto.getRefreshToken())
                .orElseThrow(() -> AuthException.INVALID_TOKEN);

        if(authToken.isExpired(requestAt)) {
            authTokenService.deleteToken(authToken);
            throw AuthException.TOKEN_ALREADY_EXPIRED;
        }

        JwtTokenDto jwtTokenDto = this.generateToken(authToken.getUserId(), requestAt);

        return TokenResponse.builder()
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }

    @Transactional
    public void logout(String userId, String accessToken) {
        // 1. push access token to blacklist
        Claims claim = jwtTokenProvider.getClaim(accessToken);
        Date expiredDate = claim.getExpiration();
        LocalDateTime expiredDateTime = toLocalDateTime(expiredDate);

        authTokenBlackListService.createBlackList(userId, expiredDateTime, accessToken);

        // 2. remove refresh token by userId
        authTokenService.deleteTokenByUserId(userId);
    }

    private JwtTokenDto generateToken(String userId, LocalDateTime requestAt) {
        Date requestDate = fromLocalDateTime(requestAt);
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(userId, requestDate);
        authTokenService.createAuthToken(userId, jwtTokenDto.getRefreshTokenExpiredAt(), jwtTokenDto.getRefreshToken());

        return jwtTokenDto;
    }
}

package com.prismworks.prism.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prismworks.prism.domain.email.model.AuthType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class AuthDto {

    @Getter
    public static class SendCodeRequest {
        @Email
        private final String email;

        private final AuthType authType;

        @JsonIgnore
        private final LocalDateTime requestAt;

        @JsonCreator
        public SendCodeRequest(String email, AuthType authType) {
            this.email = email;
            this.authType = authType;
            this.requestAt = LocalDateTime.now();
        }
    }

    @Getter
    public static class VerifyCodeRequest {
        @Email
        private final String email;

        @NotEmpty
        private final String authCode;

        private final AuthType authType;

        @JsonIgnore
        private final LocalDateTime requestAt;

        @JsonCreator
        public VerifyCodeRequest(String email, String authCode, AuthType authType) {
            this.email = email;
            this.authCode = authCode;
            this.authType = authType;
            this.requestAt = LocalDateTime.now();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class SignupRequest {
        @NotEmpty
        private final String username;

        @Email
        private final String email;

        @NotEmpty(message = "인증코드는 비어있을 수 없습니다")
        private final String authCode;

        @Pattern(
                regexp = "^(?:(?=.*[0-9])(?=.*[a-zA-Z])|(?=.*[!@#$%^&*])(?=.*[a-zA-Z])|(?=.*[0-9])(?=.*[!@#$%^&*]))[a-zA-Z0-9!@#$%^&*&]{8,20}$",
                message = "Password must be 8-20 characters long and contain at least two of the following: letters, numbers, special characters."
        )
        private final String password;
    }

    @AllArgsConstructor
    @Getter
    public static class ResetPasswordRequest {
        @Email
        private final String email;

        @NotEmpty
        private final String authCode;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])|(?=.*[A-Za-z])(?=.*[!@#$%^&*])|(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,20}$")
        private final String password;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class SignupResponse {
        private String userId;
        private String email;
    }

    @Getter
    public static class LoginRequest {
        @Email
        private final String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])|(?=.*[A-Za-z])(?=.*[!@#$%^&*])|(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,20}$")
        private final String password;

        @JsonIgnore
        private final LocalDateTime requestAt;

        @JsonCreator
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
            this.requestAt = LocalDateTime.now();
        }
    }

    @Getter
    public static class RefreshTokenRequest {
        @NotEmpty
        private final String refreshToken;

        @JsonIgnore
        private final LocalDateTime requestAt;

        @JsonCreator
        public RefreshTokenRequest(String refreshToken) {
            this.refreshToken = refreshToken;
            this.requestAt = LocalDateTime.now();
        }
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class LogoutRequest {
        private String userId;
        private String accessToken;
    }
}

package com.prismworks.prism.interfaces.auth.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.dto.AuthDto;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @GetMapping("/email/exists")
    public ApiSuccessResponse emailExists(@RequestParam String email) {
        boolean emailExists = authService.emailExists(email);
        return ApiSuccessResponse.defaultOk(emailExists);
    }

    @PostMapping("/code")
    public ApiSuccessResponse sendVerificationCode(@RequestBody @Valid AuthDto.SendCodeRequest request) {
        authService.sendAuthCode(request);
        return ApiSuccessResponse.defaultOk();
    }

    @PostMapping("/code/verification")
    public ApiSuccessResponse verifyCode(@RequestBody @Valid AuthDto.VerifyCodeRequest request) {
        authService.verifyAuthCode(request);
        return ApiSuccessResponse.defaultOk();
    }

    @PostMapping("/signup")
    public ApiSuccessResponse signUp(@RequestBody @Valid AuthDto.SignupRequest request) {
        AuthDto.SignupResponse response = authService.signup(request);
        return new ApiSuccessResponse(HttpStatus.CREATED.value(), response);
    }

    @PutMapping("/password")
    public ApiSuccessResponse resetPassword(@RequestBody @Valid AuthDto.ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiSuccessResponse.defaultOk();
    }

    @PostMapping("/login")
    public ApiSuccessResponse login(@RequestBody @Valid AuthDto.LoginRequest request) {
        AuthDto.TokenResponse response = authService.login(request);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/refresh-token")
    public ApiSuccessResponse reissueToken(@RequestBody @Valid AuthDto.RefreshTokenRequest request) {
        AuthDto.TokenResponse response = authService.reissueToken(request);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/logout")
    public ApiSuccessResponse logout(HttpServletRequest request, @CurrentUser UserContext userContext) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String userId = userContext.getUserId();

        authService.logout(userId, accessToken);

        return ApiSuccessResponse.defaultOk();
    }
}

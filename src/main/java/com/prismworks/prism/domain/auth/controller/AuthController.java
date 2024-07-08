package com.prismworks.prism.domain.auth.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.dto.AuthDto;
import com.prismworks.prism.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

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

    @PostMapping("/login")
    public ApiSuccessResponse login(@RequestBody @Valid AuthDto.LoginRequest request) {
        AuthDto.LoginResponse response = authService.login(request);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/refresh-token")
    public void reissueToken(@RequestBody @Valid AuthDto.RefreshTokenRequest request) {

    }
}

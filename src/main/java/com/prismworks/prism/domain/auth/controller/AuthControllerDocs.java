package com.prismworks.prism.domain.auth.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.dto.AuthDto;
import com.prismworks.prism.domain.auth.model.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "2. 인증 API", description = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "email 존재 여부 조회", description = "입력받은 email 존재 여부 반환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "입력받은 email이 현재 서비스에서 사용중인지에 대한 결과 반환",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Boolean.class))),
    })
    ApiSuccessResponse emailExists(String email);

    @Operation(summary = "이메일 인증 코드 발송 (동기)", description = "이메일 인증 코드 발송")
    ApiSuccessResponse sendVerificationCode(AuthDto.SendCodeRequest request);

    @Operation(summary = "이메일 인증 코드 검증", description = "이메일 인증 코드 검증")
    ApiSuccessResponse verifyCode(AuthDto.VerifyCodeRequest request);

    @Operation(summary = "회원가입", description = "회원가입")
    ApiSuccessResponse signUp(AuthDto.SignupRequest request);

    @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정")
    ApiSuccessResponse resetPassword(AuthDto.ResetPasswordRequest request);

    @Operation(summary = "로그인", description = "로그인")
    ApiSuccessResponse login(AuthDto.LoginRequest request);

    @Operation(summary = "jwt token 재발급", description = "jwt refresh token을 이용하여 jwt token 재발급")
    ApiSuccessResponse reissueToken(AuthDto.RefreshTokenRequest request);

    @Operation(summary = "로그아웃", description = "로그아웃")
    ApiSuccessResponse logout(@Parameter(hidden = true) HttpServletRequest request,
        @Parameter(hidden = true) UserContext userContext);

    @Operation(summary = "이메일 인증 코드 발송 (비동기)", description = "이메일 인증 코드 발송 (비동기)")
    ApiSuccessResponse sendVerificationCodeAsync(AuthDto.SendCodeRequest request);
}

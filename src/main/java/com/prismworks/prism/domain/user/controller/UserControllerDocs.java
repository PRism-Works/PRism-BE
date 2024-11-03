package com.prismworks.prism.domain.user.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자 API", description = "사용자 관련 API")
public interface UserControllerDocs {
    @Operation(summary = "현재 로그인된 사용자 정보 조회", description = "현재 로그인된 사용자 정보 반환")
    ApiSuccessResponse getUser(@Parameter(hidden = true) UserContext userContext);

    @Operation(summary = "사용자 profile 정보 조회", description = "userId에 해당하는 사용자 profile 정보 조회")
    ApiSuccessResponse getProfile(@PathVariable String userId);

    @Operation(summary = "현재 로그인된 사용자 프로필 정보 수정", description = "현재 로그인된 사용자 프로필 정보 수정")
    ApiSuccessResponse updateProfile(@Parameter(hidden = true) UserContext userContext,
        UserDto.UpdateProfileRequest request);
}

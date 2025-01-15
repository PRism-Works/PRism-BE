package com.prismworks.prism.interfaces.user.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.user.dto.UserDetailInfo;
import com.prismworks.prism.domain.user.dto.command.UpdateProfileCommand;
import com.prismworks.prism.interfaces.user.dto.UserDto;
import com.prismworks.prism.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserControllerDocs{

    private final UserService userService;

    @GetMapping("/me")
    public ApiSuccessResponse getUser(@CurrentUser UserContext userContext) {
        UserDetailInfo response = userService.getUserDetail(userContext.getUserId());
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/{userId}/profile")
    public ApiSuccessResponse getProfile(@PathVariable String userId) {
        UserDetailInfo response = userService.getUserDetail(userId);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PatchMapping("/profile")
    public ApiSuccessResponse updateProfile(@CurrentUser UserContext userContext,
                                            @RequestBody @Valid UserDto.UpdateProfileRequest request)
    {
        UpdateProfileCommand command = UpdateProfileCommand.builder()
            .userId(userContext.getUserId())
            .username(request.getUsername())
            .skills(request.getSkills())
            .interestJobs(request.getInterestJobs())
            .introduction(request.getIntroduction())
            .requestAt(request.getRequestAt())
            .build();
        userService.updateUserProfile(command);
        return ApiSuccessResponse.defaultOk();
    }
}

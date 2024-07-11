package com.prismworks.prism.domain.user.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiSuccessResponse getUser(@CurrentUser UserContext userContext) {
        Users user = userService.findUserById(userContext.getUserId());
        UserDto.UsersDetail response = new UserDto.UsersDetail(user.getUserId(), user.getEmail());
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/{userId}/profile")
    public ApiSuccessResponse getProfile(@PathVariable String userId) {
        UserDto.UserProfileDetail userProfile = userService.getUserProfileDetail(userId);
        return ApiSuccessResponse.defaultOk(userProfile);
    }

    @PatchMapping("/profile")
    public ApiSuccessResponse updateProfile(@CurrentUser UserContext userContext,
                                            @RequestBody @Valid UserDto.UpdateProfileRequest request)
    {
        userService.updateUserProfile(userContext.getUserId(), request);
        return ApiSuccessResponse.defaultOk();
    }
}

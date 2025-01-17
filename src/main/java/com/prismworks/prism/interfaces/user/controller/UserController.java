package com.prismworks.prism.interfaces.user.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.user.dto.UserDetailInfo;
import com.prismworks.prism.domain.user.dto.command.UpdateProfileCommand;
import com.prismworks.prism.domain.user.service.UserService;
import com.prismworks.prism.interfaces.user.dto.request.UpdateProfileRequest;
import com.prismworks.prism.interfaces.user.mapper.UserApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserControllerDocs{

    private final UserService userService;
    private final UserApiMapper userApiMapper;

    @GetMapping("/me")
    public ApiSuccessResponse getUser(@CurrentUser UserContext userContext) {
        UserDetailInfo response = userService.getUserDetailInfo(userContext.getUserId());
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/{userId}/profile")
    public ApiSuccessResponse getProfile(@PathVariable String userId) {
        UserDetailInfo response = userService.getUserDetailInfo(userId);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PatchMapping("/profile")
    public ApiSuccessResponse updateProfile(@CurrentUser UserContext userContext,
                                            @RequestBody @Valid UpdateProfileRequest request)
    {
        UpdateProfileCommand command = userApiMapper.fromUpdateProfileRequest(request,
            userContext.getUserId());
        userService.updateUserProfile(command);

        return ApiSuccessResponse.defaultOk();
    }
}

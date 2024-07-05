package com.prismworks.prism.domain.user.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.User;
import com.prismworks.prism.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ApiSuccessResponse getUser(@PathVariable String userId) {
        User user = userService.findById(userId);
        UserDto.GetUserResponse response = new UserDto.GetUserResponse(user.getUsername(), user.getEmail());
        return ApiSuccessResponse.defaultOk(response);
    }
}

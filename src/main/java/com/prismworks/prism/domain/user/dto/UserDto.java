package com.prismworks.prism.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserDto {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Create {
        private String username;
        private String email;
        private String encodedPassword;
    }

    @AllArgsConstructor
    @Getter
    public static class GetUserResponse {
        private String usernmae;
        private String email;
    }
}

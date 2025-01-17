package com.prismworks.prism.domain.user.dto;

import com.prismworks.prism.domain.user.model.Users;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfo {
    private final String userId;
    private final String email;
    private final String password;
    private final LocalDateTime createdAt;

    public UserInfo(Users user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.createdAt = user.getCreatedAt();
    }
}

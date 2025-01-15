package com.prismworks.prism.domain.user.dto.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateUserCommand {
    private final String userId;
    private final String username;
    private final String email;
    private final String encodedPassword;
}

package com.prismworks.prism.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class JwtTokenDto {
    private final String accessToken;
    private final String refreshToken;
}

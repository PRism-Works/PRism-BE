package com.prismworks.prism.domain.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = AuthToken.TABLE_NAME)
@Entity
public class AuthToken {
    public static final String TABLE_NAME = "auth_token";

    @Column(name = "auth_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer authTokenId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isExpired(LocalDateTime dateTime) {
        return expiredAt.isBefore(dateTime);
    }
}

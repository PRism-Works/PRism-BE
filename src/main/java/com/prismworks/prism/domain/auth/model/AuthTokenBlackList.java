package com.prismworks.prism.domain.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = AuthTokenBlackList.TABLE_NAME)
@Entity
public class AuthTokenBlackList {
    public static final String TABLE_NAME = "auth_token_blacklist";

    @Column(name = "auth_token_blacklist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "access_token")
    private String token;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

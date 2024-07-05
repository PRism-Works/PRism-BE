package com.prismworks.prism.domain.email.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = EmailAuthCode.TABLE_NAME)
@Entity
public class EmailAuthCode {
    public static final String TABLE_NAME = "email_auth_code";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_auth_code_id")
    @Id
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "auth_code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType authType;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void expired(LocalDateTime dateTime) {
        this.expiredAt = dateTime;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isValid(LocalDateTime dateTime) {
        return (this.verifiedAt == null) && (this.expiredAt.isAfter(dateTime));
    }

    public boolean checkCodeMatch(String code) {
        return this.code.equals(code);
    }

    public void verified(LocalDateTime dateTime) {
        this.verifiedAt = dateTime;
        this.updatedAt = LocalDateTime.now();
    }
}

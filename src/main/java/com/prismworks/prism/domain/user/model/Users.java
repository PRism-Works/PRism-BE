package com.prismworks.prism.domain.user.model;

import com.prismworks.prism.domain.user.dto.command.CreateUserCommand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = Users.TABLE_NAME)
@Entity
public class Users {
    public static final String TABLE_NAME = "user";

    @Column(name = "user_id")
    @Id
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Builder.Default
    @Column(name = "active_flag")
    private boolean isActive = true;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Users(CreateUserCommand command) {
        this.userId = command.getUserId();
        this.email = command.getEmail();
        this.password = command.getEncodedPassword();
        this.userProfile = UserProfile.builder()
                .userId(command.getUserId())
                .username(command.getUsername())
                .build();
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
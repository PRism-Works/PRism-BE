package com.prismworks.prism.domain.user.dto;

import com.prismworks.prism.domain.user.model.UserProfile;
import com.prismworks.prism.domain.user.model.Users;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDetailInfo {
    private final String userId;
    private final String email;
    private final String username;
    private final List<String> skills;
    private final List<String> interestJobs;
    private final String introduction;
    private final String role;
    private final LocalDateTime createdAt;

    public UserDetailInfo(Users user, UserProfile userProfile) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = userProfile.getUsername();
        this.skills = userProfile.getSkills();
        this.interestJobs = userProfile.getInterestJobs();
        this.introduction = userProfile.getIntroduction();
        this.role = userProfile.getRole();
        this.createdAt = user.getCreatedAt();
    }
}

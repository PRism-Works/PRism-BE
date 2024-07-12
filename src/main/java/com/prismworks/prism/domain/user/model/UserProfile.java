package com.prismworks.prism.domain.user.model;

import com.prismworks.prism.common.converter.StringToListConverter;
import com.prismworks.prism.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = UserProfile.TABLE_NAME)
@Entity
public class UserProfile {
    public static final String TABLE_NAME = "user_profile";

    @Column(name = "user_id")
    @Id
    private String userId;

    @Column(name = "username")
    private String username;

    @Convert(converter = StringToListConverter.class)
    @Column(name = "skills")
    private List<String> skills;

    @Convert(converter = StringToListConverter.class)
    @Column(name = "interest_jobs")
    private List<String> interestJobs;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "role")
    private String role;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateProfile(UserDto.UpdateProfileRequest dto) {
        boolean isUpdate = false;
        String username = dto.getUsername();
        if(StringUtils.hasText(username) && !username.equals(this.username)) {
            this.username = username;
            isUpdate = true;
        }

        List<String> skills = dto.getSkills();
        if(skills != null && !skills.isEmpty()) {
            this.skills = skills;
            isUpdate = true;
        }

        List<String> interestJobs = dto.getInterestJobs();
        if(interestJobs != null && !interestJobs.isEmpty()) {
            this.interestJobs = interestJobs;
            isUpdate = true;
        }

        String introduction = dto.getIntroduction();
        if(StringUtils.hasText(introduction) && !introduction.equals(this.introduction)) {
            this.introduction = introduction;
            isUpdate = true;
        }

        if(isUpdate) {
            this.updatedAt = dto.getRequestAt();
        }
    }
}

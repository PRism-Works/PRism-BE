package com.prismworks.prism.interfaces.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class UserDto {
    @Getter
    public static class UpdateProfileRequest {
        @Size(min = 1, max = 20)
        private final String username;

        private final List<String> skills;

        private final List<String> interestJobs;

        @Size(max = 100)
        private final String introduction;

        @JsonIgnore
        private final LocalDateTime requestAt;

        @JsonCreator
        public UpdateProfileRequest(String username, List<String> skills, List<String> interestJobs,
                                    String introduction)
        {
            this.username = username;
            this.skills = skills;
            this.interestJobs = interestJobs;
            this.introduction = introduction;
            this.requestAt = LocalDateTime.now();
        }
    }
}

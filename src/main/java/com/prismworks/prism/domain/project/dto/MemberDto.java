package com.prismworks.prism.domain.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private List<String> roles;
    private boolean anonyVisibility;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberDetailDto {
        private String userId;
        private String name;
        private String email;
        private String introduction;
        private List<String> roles;
        private List<String> strengths;
        private List<String> interestDomains;
        private int joinsProject;
        private boolean anonyVisibility;
    }
}

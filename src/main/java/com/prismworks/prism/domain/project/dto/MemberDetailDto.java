package com.prismworks.prism.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MemberDetailDto {
    private String userId;
    private String name;
    private String introduction;
    private List<String> interestDomains;
    private String email;
    private List<String> roles;
    private List<String> strengthKeywords;
    private int projectCount;
    private boolean anonyVisibility;

    public MemberDetailDto(String userId, String name, String email, List<String> roles, boolean anonyVisibility) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.anonyVisibility = anonyVisibility;
    }
}

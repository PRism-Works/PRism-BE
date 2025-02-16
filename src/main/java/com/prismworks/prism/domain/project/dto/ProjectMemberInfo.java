package com.prismworks.prism.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import com.prismworks.prism.domain.project.model.ProjectUserJoin;

@Data
@AllArgsConstructor
@Builder
public class ProjectMemberInfo {
    private String userId;
    private String name;
    private String introduction;
    private List<String> interestDomains;
    private String email;
    private List<String> roles;
    private List<String> strengthKeywords;
    private int projectCount;
    private boolean anonyVisibility;

    public ProjectMemberInfo(ProjectUserJoin member) {
        this.userId = member.getUser() != null ? member.getUser().getUserId() : "-1";
        this.name = member.getName();
        this.email = member.getEmail();
        this.roles = member.getRoles();
        this.anonyVisibility = member.getAnonyVisibility();
    }
}

package com.prismworks.prism.domain.project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectDto {
    @NotEmpty
    private String projectName;

    private String projectDescription;
    private String organizationName;
    private int memberCount;
    private List<String> categories;
    private List<String> skills;
    @NotEmpty
    private String startDate;
    @NotEmpty
    private String endDate;
    private String projectUrlLink;
    private boolean visibility;
    @NotEmpty
    private String createdBy; // 프로젝트 소유자 이메일 추가
    @NotEmpty
    private List<@Valid MemberDto> members;
}

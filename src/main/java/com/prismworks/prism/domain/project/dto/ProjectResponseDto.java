package com.prismworks.prism.domain.project.dto;

import com.prismworks.prism.domain.project.model.ProjectCategoryJoin;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class ProjectResponseDto {
    private Integer projectId;
    private String projectName;
    private String projectDescription;
    private String organizationName;
    private int memberCount;
    private Set<ProjectCategoryJoin> categories;
    private List<String> skills;
    private Date startDate;
    private Date endDate;
    private String projectUrlLink;
    private boolean urlVisibility;
    private String createdBy; // 프로젝트 소유자 이메일 추가
}

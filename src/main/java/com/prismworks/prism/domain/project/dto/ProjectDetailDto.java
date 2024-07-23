package com.prismworks.prism.domain.project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectDetailDto {
    private String projectName;
    private String organizationName;
    private String startDate;
    private String endDate;
    private String projectUrlLink;
    private boolean urlVisibility;
    private String projectDescription;
    private List<String> categories;
    private List<String> skills;
    private List<MemberDetailDto> members;
    private long anonymousCount;
}

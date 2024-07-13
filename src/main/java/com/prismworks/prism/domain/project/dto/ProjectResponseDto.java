package com.prismworks.prism.domain.project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ProjectResponseDto {
    private Integer projectId;
    private String projectName;
    private String projectDescription;
    private String organizationName;
    private int memberCount;
    private List<String> categories;
    private List<String> hashTags;
    private List<String> skills;
    private Date startDate;
    private Date endDate;
    private String projectUrlLink;
}

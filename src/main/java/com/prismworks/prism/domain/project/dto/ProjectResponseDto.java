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
    private List<String> categories;
    private Date startDate;
    private Date endDate;
}
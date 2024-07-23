package com.prismworks.prism.domain.project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SummaryProjectDto {
    private Integer projectId;
    private String projectName;
    private String organizationName;
    private String startDate;
    private String endDate;
    private List<String> categories;
    private Boolean urlVisibility;
    private String userEvaluation;
    private Integer surveyParticipants;
}
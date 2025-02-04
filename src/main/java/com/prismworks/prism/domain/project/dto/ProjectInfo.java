package com.prismworks.prism.domain.project.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProjectInfo {
    private Integer projectId;
    private String projectName;
    private String projectDescription;
    private String organizationName;
    private String startDate;
    private String endDate;
    private String projectUrlLink;
    private List<String> categories;
    private List<String> skills;
    private boolean urlVisibility;
    private Integer surveyParticipants;
    private String userEvaluation;
    private boolean anonyVisibility;
}

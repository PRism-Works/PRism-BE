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
    private List<String> hashTags;
    private List<String> skills;
    @NotEmpty
    private String startDate;
    @NotEmpty
    private String endDate;
    private String projectUrlLink;
    @NotEmpty
    private List<@Valid MemberDto> members;
}

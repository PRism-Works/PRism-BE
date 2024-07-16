package com.prismworks.prism.domain.project.dto;

import com.prismworks.prism.domain.project.model.Category;
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
    private Set<Category> categories;
    private List<String> hashTags;
    private List<String> skills;
    private Date startDate;
    private Date endDate;
    private String projectUrlLink;
}

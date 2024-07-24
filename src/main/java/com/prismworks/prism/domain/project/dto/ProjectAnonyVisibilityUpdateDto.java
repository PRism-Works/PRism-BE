package com.prismworks.prism.domain.project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectAnonyVisibilityUpdateDto {
    private int projectId;
    private boolean anonyVisibility;
}

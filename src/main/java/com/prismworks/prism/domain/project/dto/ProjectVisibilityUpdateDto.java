package com.prismworks.prism.domain.project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectVisibilityUpdateDto {
    private int projectId;
    private boolean visibility;
}

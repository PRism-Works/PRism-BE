package com.prismworks.prism.interfaces.project.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectAnonyVisibilityUpdateDto {
    private int projectId;
    private boolean anonyVisibility;
}

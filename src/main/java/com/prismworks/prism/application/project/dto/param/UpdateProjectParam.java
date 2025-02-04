package com.prismworks.prism.application.project.dto.param;

import java.util.List;

import com.prismworks.prism.domain.project.model.ProjectUserJoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UpdateProjectParam {
	private final int projectId;
	private final String projectName;
	private final String projectDescription;
	private final String organizationName;
	private final List<String> categories;
	private final List<String> skills;
	private final String projectUrlLink;
	private final boolean urlVisibility;
	private final String startDate;
	private final String endDate;
	private final String createdBy;
	private final List<ProjectUserJoin> members;
}

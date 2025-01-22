package com.prismworks.prism.domain.project.dto.command;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateProjectCommand {

	private final int projectId;
	private final String projectName;
	private final String projectDescription;
	private final String organizationName;
	private final List<String> categories;
	private final List<String> skills;
	private final String projectUrlLink;
	private final boolean urlVisibility;
	private final Date startDate;
	private final Date endDate;
	private final String createdBy;
	private final List<UpdateProjectUserJoinsCommand> members;
	private final int memberCount;
}

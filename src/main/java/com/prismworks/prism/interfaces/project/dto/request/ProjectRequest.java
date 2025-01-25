package com.prismworks.prism.interfaces.project.dto.request;

import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

	@NotEmpty
	@Size(min = 1, max = 100)
	private String projectName;

	@Size(max = 1000)
	private String projectDescription;

	private String organizationName;

	@Size(max = 3)
	private List<String> categories = Collections.emptyList();

	@Size(max = 10)
	private List<String> skills = Collections.emptyList();

	private String projectUrlLink;
	private boolean urlVisibility;

	@NotEmpty
	private String startDate;

	@NotEmpty
	private String endDate;

	private String createdBy;

	@NotEmpty
	@Size(min = 1)
	private List<ProjectUserRequest> members;

	private int memberCount;
}

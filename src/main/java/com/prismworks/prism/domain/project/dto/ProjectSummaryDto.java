package com.prismworks.prism.domain.project.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectSummaryDto {
	private String projectName;
	private String organizationName;
	private Set<String> categories;
	private List<String> skills;
	private Date startDate;
	private Date endDate;
	private String projectUrlLink;
}

package com.prismworks.prism.domain.project.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectCategoryJoin;

public class ProjectDetailInfo {

	public final Integer projectId;
	public final String projectName;
	public final String projectDescription;
	public final String organizationName;
	public final int memberCount;
	public final Set<ProjectCategoryJoin> categories;
	public final List<String> skills;
	public final Date startDate;
	public final Date endDate;
	public final String projectUrlLink;
	public final boolean urlVisibility;
	public final String createdBy;

	public ProjectDetailInfo(Project project) {
		this.projectId = project.getProjectId();
		this.projectName = project.getProjectName();
		this.projectDescription = project.getProjectDescription();
		this.organizationName = project.getOrganizationName();
		this.memberCount = project.getMemberCount();
		this.categories = project.getCategories();
		this.skills = project.getSkills();
		this.startDate = project.getStartDate();
		this.endDate = project.getEndDate();
		this.projectUrlLink = project.getProjectUrlLink();
		this.urlVisibility = project.getUrlVisibility();
		this.createdBy = project.getCreatedBy();
	}

}

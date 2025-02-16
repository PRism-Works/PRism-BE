package com.prismworks.prism.domain.project.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.prismworks.prism.domain.project.model.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProjectDetailInfo {

	public final Integer projectId;
	public final String projectName;
	public final String projectDescription;
	public final String organizationName;
	public final int memberCount;
	public final List<String> categories;
	public final List<String> skills;
	public final String startDate;
	public final String endDate;
	public final String projectUrlLink;
	public final boolean urlVisibility;
	private String mostCommonTraits;
	private List<ProjectMemberInfo> members;
	private long anonymousCount;
	public final String createdBy;

	public ProjectDetailInfo(Project project) {
		this.projectId = project.getProjectId();
		this.projectName = project.getProjectName();
		this.projectDescription = project.getProjectDescription();
		this.organizationName = project.getOrganizationName();
		this.memberCount = project.getMemberCount();
		this.categories = project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList());
		this.skills = project.getSkills();
		this.startDate = formatDate(project.getStartDate());
		this.endDate = formatDate(project.getEndDate());
		this.projectUrlLink = project.getProjectUrlLink();
		this.urlVisibility = project.getUrlVisibility();
		this.createdBy = project.getCreatedBy();
	}

	public ProjectDetailInfo(Project project, List<ProjectMemberInfo> projectMemberInfos, long anonymousCount) {
		this.projectId = project.getProjectId();
		this.projectName = project.getProjectName();
		this.projectDescription = project.getProjectDescription();
		this.organizationName = project.getOrganizationName();
		this.startDate = formatDate(project.getStartDate());
		this.endDate = formatDate(project.getEndDate());
		this.projectUrlLink = project.getProjectUrlLink();
		this.urlVisibility = project.getUrlVisibility();
		this.mostCommonTraits = "";
		this.categories = project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList());
		this.skills = project.getSkills();
		this.members = projectMemberInfos;
		this.anonymousCount = anonymousCount;
		this.createdBy = project.getCreatedBy();
		this.memberCount = projectMemberInfos.size();
	}

	private String formatDate(Date date) {  // TODO Util로 빼서 공통으로 사용
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

}

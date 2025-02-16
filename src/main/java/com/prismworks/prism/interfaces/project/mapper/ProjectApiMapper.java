package com.prismworks.prism.interfaces.project.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.prismworks.prism.application.project.dto.param.UpdateProjectParam;
import com.prismworks.prism.domain.project.dto.command.ProjectCategoryCommonCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectCommand;
import com.prismworks.prism.domain.project.dto.command.CreateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectMemberCommonCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.dto.query.ProjectInfoQuery;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.interfaces.project.dto.request.ProjectInfoRequest;
import com.prismworks.prism.interfaces.project.dto.request.ProjectRequest;
import com.prismworks.prism.interfaces.project.dto.request.UpdateProjectRequest;

@Component
public class ProjectApiMapper {

	public UpdateProjectParam fromUpdateProjectParam(
		UpdateProjectRequest request,
		int projectId,
		String userEmail
	) {
		return UpdateProjectParam.builder()
			.projectId(projectId)
			.projectName(request.getProjectName())
			.projectDescription(request.getProjectDescription())
			.organizationName(request.getOrganizationName())
			.skills(request.getSkills())
			.projectUrlLink(request.getProjectUrlLink())
			.urlVisibility(request.isUrlVisibility())
			.startDate(request.getStartDate())
			.endDate(request.getEndDate())
			.createdBy(userEmail)
			.categories(request.getCategories())
			.members(request.getMembers())
			.build();
	}

	public UpdateProjectCommand projectRequestToUpdateCommand(
		ProjectRequest request,
		int projectId,
		String userEmail
	) {
		validateRequest(request);

		UpdateProjectCommand.UpdateProjectCommandBuilder<?, ?> builder = UpdateProjectCommand.builder();
		buildCommonProjectCommand(request, userEmail, builder);

		return builder
			.projectId(projectId)
			.build();
	}

	public CreateProjectCommand projectRequestToCreateCommand(ProjectRequest request, String userEmail) {
		validateRequest(request);

		CreateProjectCommand.CreateProjectCommandBuilder<?, ?> builder = CreateProjectCommand.builder();
		buildCommonProjectCommand(request, userEmail, builder);

		return builder.build();
	}

	public ProjectInfoQuery projectRequestToInfoQuery(ProjectInfoRequest request) { //TODO ProjectRequest로 대체, ProjectInfoRequest 제거
		return ProjectInfoQuery.builder()
			.projectName(request.getProjectName())
			.memberName(request.getMemberName())
			.categories(request.getCategories())
			.organizationName(request.getOrganizationName())
			.build();
	}

	private void buildCommonProjectCommand(
		ProjectRequest request,
		String userEmail,
		ProjectCommand.ProjectCommandBuilder<?, ?> builder
	) {
		builder.projectName(request.getProjectName())
			.projectDescription(request.getProjectDescription())
			.organizationName(request.getOrganizationName())
			.skills(request.getSkills())
			.projectUrlLink(request.getProjectUrlLink())
			.urlVisibility(request.isUrlVisibility())
			.startDate(parseDate(request.getStartDate()))
			.endDate(parseDate(request.getEndDate()))
			.createdBy(userEmail)
			.categories(request.getCategories().stream()
				.map(categoryName -> ProjectCategoryCommonCommand.builder()
					.categoryName(categoryName)
					.build())
				.toList())
			.members(request.getMembers().stream()
				.map(member -> ProjectMemberCommonCommand.builder()
					.name(member.getName())
					.email(member.getEmail())
					.roles(member.getRoles())
					.anonyVisibility(member.isAnonyVisibility())
					.build())
				.toList());
	}

	private static void validateRequest(ProjectRequest request) {
		if (request.getProjectName() == null || request.getProjectName().isEmpty()) {
			throw ProjectException.NO_PROJECT_NAME;
		}

		if (request.getMembers() == null || request.getMembers().isEmpty()) {
			throw ProjectException.NO_MEMBER;
		}

		if (request.getStartDate() == null || request.getEndDate() == null) {
			throw ProjectException.NO_DATETIME;
		}
	}

	private Date parseDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new ProjectException(ProjectErrorCode.INVALID_DATE_FORMAT);
		}
	}

}

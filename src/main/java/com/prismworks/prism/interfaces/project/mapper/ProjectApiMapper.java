package com.prismworks.prism.interfaces.project.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.prismworks.prism.domain.project.dto.command.ProjectCommand;
import com.prismworks.prism.domain.project.dto.command.CreateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectUserJoinsCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.interfaces.project.dto.request.ProjectRequest;

@Component
public class ProjectApiMapper {

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

	private void buildCommonProjectCommand(
		ProjectRequest request,
		String userEmail,
		ProjectCommand.ProjectCommandBuilder<?, ?> builder
	) {
		builder.projectName(request.getProjectName())
			.projectDescription(request.getProjectDescription())
			.organizationName(request.getOrganizationName())
			.categories(request.getCategories())
			.skills(request.getSkills())
			.projectUrlLink(request.getProjectUrlLink())
			.urlVisibility(request.isUrlVisibility())
			.startDate(parseDate(request.getStartDate()))
			.endDate(parseDate(request.getEndDate()))
			.createdBy(userEmail)
			.members(request.getMembers().stream()
				.map(member -> UpdateProjectUserJoinsCommand.builder()
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

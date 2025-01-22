package com.prismworks.prism.interfaces.project.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.prismworks.prism.domain.project.dto.command.UpdateProjectUserJoinsCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.interfaces.project.dto.request.UpdateProjectRequest;

@Component
public class ProjectApiMapper {
	public UpdateProjectCommand fromUpdateProjectRequest(
		UpdateProjectRequest request
		, int projectId
		, String userEmail
	) {

		if (request.getProjectName() == null || request.getProjectName().isEmpty()) {
			throw ProjectException.NO_PROJECT_NAME;
		}

		if (request.getMembers() == null || request.getMembers().isEmpty()) {
			throw ProjectException.NO_MEMBER;
		}

		if (request.getStartDate() == null || request.getEndDate() == null) {
			throw ProjectException.NO_DATETIME;
		}

		return UpdateProjectCommand.builder()
			.projectId(projectId)
			.projectName(request.getProjectName())
			.projectDescription(request.getProjectDescription())
			.organizationName(request.getOrganizationName())
			.categories(request.getCategories())
			.skills(request.getSkills())
			.projectUrlLink(request.getProjectUrlLink())
			.urlVisibility(request.isUrlVisibility())
			.startDate(parseDate(request.getStartDate()))
			.endDate(parseDate(request.getStartDate()))
			.createdBy(userEmail)
			.members(request.getMembers().stream()
				.map(member -> UpdateProjectUserJoinsCommand.builder()
					.name(member.getName())
					.email(member.getEmail())
					.roles(member.getRoles())
					.anonyVisibility(member.isAnonyVisibility())
					.build())
				.toList()
			)
			.memberCount(request.getMemberCount())
			.build();
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

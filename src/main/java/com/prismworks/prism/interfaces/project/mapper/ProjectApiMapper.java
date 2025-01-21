package com.prismworks.prism.interfaces.project.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.prismworks.prism.domain.project.dto.command.ProjectUserCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.interfaces.project.dto.request.UpdateProjectRequest;

@Component
public class ProjectApiMapper {
	public UpdateProjectCommand fromUpdateProjectRequest(
		UpdateProjectRequest request
		, int projectId
	) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		return UpdateProjectCommand.builder()
			.projectId(projectId)
			.projectName(request.getProjectName())
			.projectDescription(request.getProjectDescription())
			.organizationName(request.getOrganizationName())
			.categories(request.getCategories())
			.skills(request.getSkills())
			.projectUrlLink(request.getProjectUrlLink())
			.urlVisibility(request.isUrlVisibility())
			.startDate(sdf.parse(request.getStartDate()))
			.endDate(sdf.parse(request.getStartDate()))
			.createdBy(request.getCreatedBy())
			.members(request.getMembers().stream()
				.map(member -> ProjectUserCommand.builder()
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
}

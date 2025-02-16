package com.prismworks.prism.application.project.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.prismworks.prism.application.project.dto.param.UpdateProjectParam;
import com.prismworks.prism.domain.project.dto.command.ProjectCategoryCommonCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectMemberCommonCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;

@Component
public class ProjectApplicationMapper {

	public UpdateProjectCommand toUpdateProjectCommand(UpdateProjectParam param) {
		UpdateProjectCommand.UpdateProjectCommandBuilder<?, ?> builder = UpdateProjectCommand.builder();
		buildCommonProjectCommand(param, builder);

		return builder
			.projectId(param.getProjectId())
			.build();
	}

	private void buildCommonProjectCommand(
		UpdateProjectParam request,
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
			.createdBy(request.getCreatedBy())
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
					.anonyVisibility(member.getAnonyVisibility())
					.build())
				.toList());
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

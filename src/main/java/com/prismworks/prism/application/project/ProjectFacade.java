package com.prismworks.prism.application.project;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prismworks.prism.application.project.dto.param.UpdateProjectParam;
import com.prismworks.prism.application.project.dto.result.UpdateProjectResult;
import com.prismworks.prism.application.project.mapper.ProjectApplicationMapper;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProjectFacade {
	private final UserRepository userRepository;
	private final ProjectService projectService;
	private final ProjectApplicationMapper mapper;

	@Transactional
	public UpdateProjectResult updateProject(UpdateProjectParam param) {
		UpdateProjectCommand command = mapper.toUpdateProjectCommand(param);

		setProjectMember(command);

		setProjectCategoryByName(command);

		return new UpdateProjectResult(projectService.updateProject(command));

	}

	private void setProjectMember(UpdateProjectCommand command) {
		command.getMembers().forEach(memberCommand ->
			userRepository.getUserByEmail(memberCommand.getEmail()) // TODO userService로 대체 -> public 메서드 필요
				.ifPresent(memberCommand::setUser));
	}

	private void setProjectCategoryByName(UpdateProjectCommand command) {
		command.getCategories().forEach(
			categoryCommand ->
				projectService.getCategoryByName(categoryCommand.getCategoryName())
					.ifPresent(categoryCommand::setCategory));
	}
}

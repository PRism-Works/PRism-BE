package com.prismworks.prism.domain.project.dto.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UpdateProjectCommand extends ProjectCommand {

	private final int projectId;
}

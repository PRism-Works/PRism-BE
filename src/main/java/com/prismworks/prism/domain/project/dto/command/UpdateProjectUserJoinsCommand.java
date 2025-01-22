package com.prismworks.prism.domain.project.dto.command;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateProjectUserJoinsCommand {

	@NotEmpty
	private final String name;

	@NotEmpty
	private final String email;

	@NotEmpty
	private final List<String> roles;

	private final boolean anonyVisibility;
}

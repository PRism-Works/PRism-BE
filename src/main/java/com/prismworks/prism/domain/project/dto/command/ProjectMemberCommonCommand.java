package com.prismworks.prism.domain.project.dto.command;

import com.prismworks.prism.domain.user.model.Users;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProjectMemberCommonCommand {

	@NotEmpty
	private final String name;

	@NotEmpty
	private final String email;

	private Users user;

	@NotEmpty
	private final List<String> roles;

	private final boolean anonyVisibility;

	public void setUser(Users user) {
		this.user = user;
	}
}

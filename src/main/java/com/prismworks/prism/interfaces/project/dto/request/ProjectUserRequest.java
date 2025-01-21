package com.prismworks.prism.interfaces.project.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserRequest {
	@NotEmpty
	private String name;
	@NotEmpty
	private String email;
	@NotEmpty
	private List<String> roles;
	private boolean anonyVisibility;
}

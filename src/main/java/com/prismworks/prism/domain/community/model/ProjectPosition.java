package com.prismworks.prism.domain.community.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectPosition {
	PLANNER("planner"),
	DESIGNER("designer");

	private final String value;

	@JsonCreator
	public static ProjectPosition from(String position) {
		return Arrays.stream(ProjectPosition.values())
			.filter(projectPosition -> projectPosition.value.equals(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown status: " + position));
	}
}

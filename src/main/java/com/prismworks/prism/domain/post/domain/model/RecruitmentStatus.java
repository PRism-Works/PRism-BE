package com.prismworks.prism.domain.post.domain.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitmentStatus {
	RECRUITING("recuriting"),
	CLOSED("closed");

	private final String description;

	@JsonCreator
	public static RecruitmentStatus from(String status) {
		return Arrays.stream(RecruitmentStatus.values())
			.filter(recruitmentStatus -> recruitmentStatus.description.equals(status))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown status: " + status));
	}
}

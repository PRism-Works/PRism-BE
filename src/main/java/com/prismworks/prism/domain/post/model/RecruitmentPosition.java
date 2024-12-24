package com.prismworks.prism.domain.post.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitmentPosition {
	PM("pm"),
	MARKETER("marketer"),
	FRONTEND("frontend"),
	BACKEND("backend"),
	FULLSTACK("fullstack"),
	DESIGNER("designer"),
	IOS("iOS"),
	ANDROID("android"),
	DEVOPS("devops"),
	QA("qa");

	private final String value;

	@JsonValue
	public String getValue() {
		return this.value;
	}

	@JsonCreator
	public static RecruitmentPosition from(String position) {
		return Arrays.stream(RecruitmentPosition.values())
			.filter(recruitmentPosition -> recruitmentPosition.value.equals(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown status: " + position));
	}
}

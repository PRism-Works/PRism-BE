package com.prismworks.prism.domain.post.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitmentPosition {
	PM("기획자/PM"),
	MARKETER("마케터"),
	FRONTEND("프론트엔드"),
	BACKEND("백엔드"),
	FULLSTACK("풀스택"),
	DESIGNER("디자이너"),
	IOS("iOS"),
	ANDROID("Android"),
	DEVOPS("devops"),
	QA("QA");

	private final String value;

	@JsonCreator
	public static RecruitmentPosition from(String position) {
		return Arrays.stream(RecruitmentPosition.values())
			.filter(recruitmentPosition -> recruitmentPosition.value.equals(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown status: " + position));
	}
}

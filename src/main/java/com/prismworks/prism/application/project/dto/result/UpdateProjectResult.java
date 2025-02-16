package com.prismworks.prism.application.project.dto.result;

import com.prismworks.prism.domain.project.dto.ProjectDetailInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdateProjectResult {
	private final ProjectDetailInfo projectDetailInfo;
}

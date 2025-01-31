package com.prismworks.prism.domain.project.dto.query;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectInfoQuery {
	private String projectName;
	private String memberName;
	private List<String> categories;
	private String organizationName;
}

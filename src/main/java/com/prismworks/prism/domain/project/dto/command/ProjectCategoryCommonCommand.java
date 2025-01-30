package com.prismworks.prism.domain.project.dto.command;

import com.prismworks.prism.domain.project.model.Category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProjectCategoryCommonCommand {

	Category category;

	@NotEmpty
	private final String categoryName;

	public void setCategory(Category category) {
		this.category = category;
	}
}

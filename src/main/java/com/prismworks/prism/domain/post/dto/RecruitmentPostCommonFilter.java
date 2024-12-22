package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.ProjectPosition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentPostCommonFilter {
    private ProjectPosition position;
    private String category;
    private ProcessMethod processMethod;
    private String skill;
}

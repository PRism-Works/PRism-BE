package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ProjectPosition;
import com.prismworks.prism.domain.post.model.ProjectProcessMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentPostCommonFilter {
    private ProjectPosition position;
    private String category;
    private ProjectProcessMethod processMethod;
    private String skill;
}

package com.prismworks.prism.domain.post.application.dto.result;

import com.prismworks.prism.domain.post.domain.dto.PostRecruitmentInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WritPostResult {
    private final PostRecruitmentInfo postRecruitmentInfo;
}

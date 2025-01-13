package com.prismworks.prism.application.post.dto.result;

import com.prismworks.prism.domain.post.dto.PostRecruitmentInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WritPostResult {
    private final PostRecruitmentInfo postRecruitmentInfo;
}

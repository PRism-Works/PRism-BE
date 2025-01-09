package com.prismworks.prism.domain.post.interfaces.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateRecruitmentPostResponse {
    private final Long postId;
}

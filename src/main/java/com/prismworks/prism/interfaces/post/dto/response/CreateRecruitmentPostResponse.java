package com.prismworks.prism.interfaces.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateRecruitmentPostResponse {
    private final Long postId;
}

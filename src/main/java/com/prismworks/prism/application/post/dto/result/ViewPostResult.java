package com.prismworks.prism.application.post.dto.result;

import com.prismworks.prism.domain.post.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.project.dto.ProjectDetailInfo;
import com.prismworks.prism.domain.user.dto.UserDetailInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ViewPostResult {
    private final PostRecruitmentInfo postRecruitmentInfo;
    private final UserDetailInfo writerInfo;
    private final ProjectDetailInfo projectInfo;
}

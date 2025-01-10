package com.prismworks.prism.application.post.dto.result;

import com.prismworks.prism.domain.post.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.interfaces.user.dto.UserDto.UserProfileDetail;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ViewPostResult {
    private final PostRecruitmentInfo postRecruitmentInfo;
    private final UserProfileDetail writerInfo;
    private final ProjectDetailDto projectInfo;
}

package com.prismworks.prism.domain.post.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RecruitmentPostInfo {
    private final Long postId;
    private final String title;
    private final String content;
    private final List<String> categories;
    private final List<RecruitmentPosition> positions;
    private final boolean isOpenUntilRecruited;
    private final LocalDateTime recruitmentStartAt;
    private final LocalDateTime recruitmentEndAt;
    private final int viewCount;
    private final boolean isBookmarked;
    private final UserInfo userInfo;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private String userId;
        private String username;
        private String email;
    }
}

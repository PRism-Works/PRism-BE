package com.prismworks.prism.interfaces.post.dto.response;

import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo.UserInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SearchRecruitmentPostsResponse {
    private final long totalCount;
    private final int totalPages;
    private final int currentPage;
    private final List<SearchRecruitmentPostItem> posts;

    @Getter
    @RequiredArgsConstructor
    public static class SearchRecruitmentPostItem {
        @Schema(description = "게시글 ID")
        private final Long postId;

        @Schema(description = "게시글 제목")
        private final String title;

        @Schema(description = "게시글 내용")
        private final String content;

        @Schema(description = "카테고리 목록")
        private final List<String> categories;

        @Schema(description = "모집 직무 목록")
        private final List<RecruitmentPosition> positions;

        @Schema(description = "모집 마감시까지 계속 모집 여부")
        private final boolean isOpenUntilRecruited;

        @Schema(description = "모집 시작일")
        private final LocalDateTime recruitmentStartAt;

        @Schema(description = "모집 마감일")
        private final LocalDateTime recruitmentEndAt;

        @Schema(description = "조회수")
        private final int viewCount;

        @Schema(description = "북마크 여부")
        private final boolean isBookmarked;

        @Schema(description = "북마크 개수")
        private final int bookmarkCount;

        @Schema(description = "작성자 정보")
        private final UserInfo writerInfo;

        public SearchRecruitmentPostItem(SearchRecruitmentPostInfo postInfo) {
            this.postId = postInfo.getPostId();
            this.title = postInfo.getTitle();
            this.content = postInfo.getContent();
            this.categories = postInfo.getCategories();
            this.positions = postInfo.getPositions();
            this.isOpenUntilRecruited = postInfo.isOpenUntilRecruited();
            this.recruitmentStartAt = postInfo.getRecruitmentStartAt();
            this.recruitmentEndAt = postInfo.getRecruitmentEndAt();
            this.viewCount = postInfo.getViewCount();
            this.isBookmarked = postInfo.isBookmarked();
            this.bookmarkCount = postInfo.getBookmarkCount();
            this.writerInfo = postInfo.getUserInfo();
        }
    }
}

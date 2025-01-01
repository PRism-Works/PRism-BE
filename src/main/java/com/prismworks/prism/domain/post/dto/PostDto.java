package com.prismworks.prism.domain.post.dto;

import static com.prismworks.prism.domain.post.dto.command.PostCommand.CreatePost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prismworks.prism.domain.post.dto.command.PostTeamRecruitmentCommand.CreatePostTeamRecruitment;
import com.prismworks.prism.domain.post.dto.command.TeamRecruitmentPositionCommand.CreateTeamRecruitmentPosition;
import com.prismworks.prism.domain.post.dto.query.PostQuery;
import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo.UserInfo;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class PostDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRecruitmentPostRequest {
        @Schema(description = "마감일 팀원 모집 완료시 까지 여부")
        private boolean openUntilRecruited;

        @Schema(description = "모집 시작일, isOpenUntilRecruited가 true일 경우 빈값", example = "2021-08-01T00:00:00")
        private LocalDateTime recruitmentStartAt;

        @Schema(description = "모집 마감일, isOpenUntilRecruited가 true일 경우 빈값", example = "2021-08-31T23:59:59")
        private LocalDateTime recruitmentEndAt;

        @Schema(description = "게시글 제목")
        private String title;

        @Schema(description = "게시글 내용")
        private String content;

        @Schema(description = "프로젝트 ID", type = "integer")
        private Integer projectId;

        @Schema(description = "연락 방식", allowableValues = {"kakaoTalk", "email", "googleForm"}, example = "kakaoTalk")
        private ContactMethod contactMethod;

        @Schema(description = "연락 방식 직접 입력 정보")
        private String contactInfo;

        @Schema(description = "신청 방식", allowableValues = {"kakaoTalk", "email", "googleForm"}, example = "kakaoTalk")
        private ApplyMethod applyMethod;

        @Schema(description = "신청 방식 직접 입력 정보")
        private String applyInfo;

        @Schema(description = "진행 방법", allowableValues = {"online", "offline", "onlineAndOffline", "etc"})
        private ProcessMethod processMethod;

        @Parameter(description = "모집 직무 및 인원<br/>"
            + "pm, marketer, frontend, backend, fullstack, designer, ios, android, devops, qa",
            array = @ArraySchema(schema = @Schema(implementation = RecruitPositionItem.class)))
        @ArraySchema(schema = @Schema(implementation = RecruitPositionItem.class))
        private List<RecruitPositionItem> recruitPositions;

        public CreatePost toCreatePostCommand(String userId) {
            return CreatePost.builder()
                .userId(userId)
                .title(this.title)
                .content(this.content)
                .build();
        }

        public CreatePostTeamRecruitment toCreatePostTeamRecruitmentCommand(Post post,
            List<TeamRecruitmentPosition> positions
        ) {
            return CreatePostTeamRecruitment.builder()
                .post(post)
                .projectId(this.projectId)
                .contactMethod(this.contactMethod)
                .contactInfo(this.contactInfo)
                .applyMethod(this.applyMethod)
                .applyInfo(this.applyInfo)
                .processMethod(this.processMethod)
                .recruitmentPositions(positions)
                .isOpenUntilRecruited(this.openUntilRecruited)
                .recruitmentStartAt(this.recruitmentStartAt)
                .recruitmentEndAt(this.recruitmentEndAt)
                .build();
        }

        public List<CreateTeamRecruitmentPosition> toCreateTeamRecruitmentPositionCommand() {
            return recruitPositions.stream()
                .map(positionInfo -> CreateTeamRecruitmentPosition.builder()
                    .position(positionInfo.getPosition())
                    .recruitmentCount(positionInfo.getCount())
                    .build())
                .toList();
        }
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "주소 정보 객체")
    public static class RecruitPositionItem {
        @Schema(description = "모집 직무",
            allowableValues = {"pm", "marketer", "frontend", "backend", "fullstack", "designer", "ios", "android", "devops", "qa"})
        private final RecruitmentPosition position;

        @Schema(description = "모집 인원")
        private final int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateRecruitmentPostResponse {
        private final Long postId;
        private final LocalDateTime recruitStartAt;
        private final LocalDateTime recruitEndAt;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ContactMethod applyMethod;
        private final String applyInfo;
        private final ProcessMethod processMethod;
        private final List<RecruitPositionItem> recruitPositions;
        private final String title;
        private final String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchRecruitmentPostsRequest {
        @Parameter(description = "직무 선택 필터 - 전체 조건일때는 빈 배열로 전달<br/>"
            + "pm, marketer, frontend, backend, fullstack, designer, ios, android, devops, qa",
            allowEmptyValue = true,
            array = @ArraySchema(arraySchema = @Schema(type = "string"))
        )
        private List<RecruitmentPosition> recruitmentPositions;

        @Parameter(description = "카테고리 선택 필터 (카테고리 ID) - 전체 조건일때는 빈 배열로 전달<br/>"
            + "1(금융), 2(헬스케어), 3(교육), 4(커머스), 5(여행), 6(엔터테이먼트), 7(생산성), 8(기타)",
            allowEmptyValue = true)
        private List<Integer> categoryIds;

        @Parameter(description = "진행 방식 선택 필터 - 전체 조건일때는 빈 값으로 전달", allowEmptyValue = true)
        private ProcessMethod processMethod;

        @Parameter(description = "기술스택 선택 필터 - 전체 조건일때는 빈 배열로 전달", allowEmptyValue = true)
        private List<String> skills;

        @Schema(description = "모집 여부 필터 - 모집 중: true / 이외: false", defaultValue = "false")
        private boolean recruiting;

        @Schema(description = "페이지 번호", defaultValue = "0")
        private int pageNo = 0;

        @Schema(description = "페이지 사이즈", defaultValue = "10")
        private int pageSize = 10;

        public PostQuery.GetRecruitmentPosts toGetRecruitmentPostsQuery(String userId) {
            return PostQuery.GetRecruitmentPosts.builder()
                .recruitmentPositions(recruitmentPositions)
                .categoryIds(categoryIds)
                .processMethod(processMethod)
                .skills(skills)
                .recruitmentStatuses(recruiting ? List.of(RecruitmentStatus.RECRUITING)
                    : List.of(RecruitmentStatus.RECRUITING, RecruitmentStatus.CLOSED))
                .isBookmarkSearch(false)
                .userId(userId)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
        }
    }

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

        @Schema(description = "작성자 정보")
        private final UserInfo writerInfo;

        public SearchRecruitmentPostItem(RecruitmentPostInfo postInfo) {
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
            this.writerInfo = postInfo.getUserInfo();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SearchRecruitmentPostsResponse {
        private final long totalCount;
        private final int totalPages;
        private final int currentPage;
        private final List<SearchRecruitmentPostItem> posts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchBookmarkedRecruitmentPostsRequest extends SearchRecruitmentPostsRequest {
        public PostQuery.GetRecruitmentPosts toGetRecruitmentPostsQuery(String userId) {
            return PostQuery.GetRecruitmentPosts.builder()
                .recruitmentPositions(super.recruitmentPositions)
                .categoryIds(super.categoryIds)
                .processMethod(super.processMethod)
                .skills(super.skills)
                .recruitmentStatuses(super.recruiting ? List.of(RecruitmentStatus.RECRUITING)
                    : List.of(RecruitmentStatus.RECRUITING, RecruitmentStatus.CLOSED))
                .isBookmarkSearch(true)
                .userId(userId)
                .pageNo(super.pageNo)
                .pageSize(super.pageSize)
                .build();
        }
    }

    @Getter
    @Builder
    public static class RecruitmentPostDetailDto {
        private Long postId;
        private Integer projectId;
        private Long postTeamRecruitmentId;
        private final RecruitmentStatus recruitmentStatus;
        private final String title;
        private final String content;
        private final String userId;
        private UserDto.UserProfileDetail writer;
        private final int viewCount;
        private LocalDateTime createdAt;
        private final LocalDateTime recruitmentStart;
        private final LocalDateTime recruitmentEnd;
        private final ProcessMethod processMethod;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ApplyMethod applicationMethod;
        private final String applicationInfo;
        private final Set<TeamRecruitmentPosition> recruitmentPositions;

        public static RecruitmentPostDetailDto of(
            Post post,
            PostTeamRecruitment recruitment
        ) {
            return RecruitmentPostDetailDto.builder()
                .postId(post.getPostId())
                .projectId(recruitment.getProjectId())
                .postTeamRecruitmentId(recruitment.getPostTeamRecruitmentId())
                .recruitmentStatus(recruitment.getRecruitmentStatus())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .recruitmentStart(recruitment.getRecruitmentStartAt())
                .recruitmentEnd(recruitment.getRecruitmentEndAt())
                .contactMethod(recruitment.getContactMethod())
                .contactInfo(recruitment.getContactInfo())
                .applicationMethod(recruitment.getApplyMethod())
                .applicationInfo(recruitment.getApplyInfo())
                .processMethod(recruitment.getProcessMethod())
                .recruitmentPositions(recruitment.getRecruitmentPositions())
                .build();
        }

        public void setWriter(UserDto.UserProfileDetail writer) {
            this.writer = writer;
        }
    }

    @Builder
    @Getter
    public static class ViewPostDto {
        private RecruitmentPostDetailDto post;
        private ProjectDetailDto project;
    }
}

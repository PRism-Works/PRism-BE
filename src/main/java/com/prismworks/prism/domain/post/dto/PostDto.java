package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRecruitmentPostRequest {
        private LocalDateTime recruitStartAt;
        private LocalDateTime recruitEndAt;
        private ContactMethod contactMethod;
        private String contactInfo;
        private ContactMethod applyMethod;
        private String applyInfo;
        private ProcessMethod processMethod;
        private List<RecruitPositionItem> recruitPositions;
        private String title;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class RecruitPositionItem {
        private final RecruitmentPosition position;
        private final int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateRecruitmentPostResponse { // todo: response에 id 추가
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
    @Builder
    @AllArgsConstructor
    public static class GetMyRecruitmentPostsResponse {
        private final Long postId;
        private final List<RecruitmentPosition> positions;
        private final String title;
        private final List<String> categories;
        private final LocalDateTime recruitEndAt;
        private final int viewCount;
    }

    @Getter
    @Builder
    public static class RecruitmentPostDetailDto {
        private Long postId;
        private final RecruitmentStatus recruitmentStatus;
        private final String title;
        private final String content;
        private final String writer;
        private final int viewCount;
        private LocalDateTime createdAt;
        private final LocalDateTime recruitmentStart;
        private final LocalDateTime recruitmentEnd;
        private final ProcessMethod processMethod;
        private final List<RecruitPositionItem> recruitmentPositions;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ApplyMethod applicationMethod;
        private final String applicationInfo;

//        public static RecruitmentPostDetailDto of(Post post, PostTeamRecruitment recruitment) {
//            return RecruitmentPostDetailDto.builder()
//                .postId(post.getPostId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .writer(post.getWriter())
//                .viewCount(post.getViewCount())
//                .createdAt(post.getCreatedAt())
//                .recruitmentStart(recruitment.getRecruitmentStart())
//                .recruitmentEnd(recruitment.getRecruitmentEnd())
//                .contactMethod(ContactMethod.from(recruitment.getContactMethod().toString()))
//                .contactInfo(recruitment.getContactInfo())
//                .applicationMethod(ApplicationMethod.from(recruitment.getApplicationMethod().toString()))
//                .applicationInfo(recruitment.getApplicationInfo())
//                .processMethod(ProcessMethod.from(recruitment.getProcessMethod().toString()))
//                .recruitmentPositions(
//                    recruitment.getRecruitmentPositions().stream()
//                        .map(position -> new RecruitPositionItem(ProjectPosition.from(position.getPosition()), position.getPositionSize()))
//                        .collect(Collectors.toList())
//                )
//                .build();
//        }
    }
}

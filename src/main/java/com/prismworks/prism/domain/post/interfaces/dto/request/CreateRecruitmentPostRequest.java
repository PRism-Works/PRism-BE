package com.prismworks.prism.domain.post.interfaces.dto.request;

import com.prismworks.prism.domain.post.domain.model.ApplyMethod;
import com.prismworks.prism.domain.post.domain.model.ContactMethod;
import com.prismworks.prism.domain.post.domain.model.ProcessMethod;
import com.prismworks.prism.domain.post.domain.model.RecruitmentPosition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRecruitmentPostRequest {
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
}

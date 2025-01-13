package com.prismworks.prism.interfaces.post.dto.request;

import com.prismworks.prism.domain.post.dto.query.RecruitmentPostSortOption;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchRecruitmentPostsRequest {
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

    @Schema(description = "페이지 정렬 조건", defaultValue = "recent", allowableValues = {"recent", "popular", "expiry"})
    private RecruitmentPostSortOption sort = RecruitmentPostSortOption.RECENT;
}

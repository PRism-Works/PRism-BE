package com.prismworks.prism.interfaces.project.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoRequest {
	
	@Schema(description = "프로젝트명")
	private String projectName;

	@Schema(description = "프로젝트 참가 멤버명, isOpenUntilRecruited가 true일 경우 빈값")
	private String memberName;

	@Schema(description = "카테고리 선택 필터 (카테고리명), isOpenUntilRecruited가 true일 경우 빈값"
		+ "1(금융), 2(헬스케어), 3(교육), 4(커머스), 5(여행), 6(엔터테이먼트), 7(생산성), 8(기타)")
	private List<String> categories;

	@Schema(description = "프로젝트 organization 명")
	private String organizationName;
}

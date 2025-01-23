package com.prismworks.prism.interfaces.project.dto.request;

import com.prismworks.prism.domain.project.dto.MemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @NotEmpty
    private String projectName;

    private String projectDescription;
    private String organizationName;
    private int memberCount;
    private List<String> categories;
    private List<String> skills;
    @NotEmpty
    private String startDate;
    @NotEmpty
    private String endDate;
    private String projectUrlLink;
    private boolean urlVisibility;
    private String createdBy; // 프로젝트 소유자 이메일 추가
    @NotEmpty
    private List<@Valid MemberDto> members;
}

package com.prismworks.prism.domain.community.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.community.dto.CommunityDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.community.dto.CommunityDto.CreateRecruitmentPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
@RestController
public class CommunityController {

    @PostMapping("/team-recruitment")
    public ApiSuccessResponse createRecruitmentPost(@RequestBody CreateRecruitmentPostRequest request) {
        return ApiSuccessResponse.defaultOk(CreateRecruitmentPostResponse.builder()
            .recruitmentStartAt(request.getRecruitmentStartAt())
            .recruitmentEndAt(request.getRecruitmentEndAt())
            .contactMethod(request.getContactMethod())
            .contactInfo(request.getContactInfo())
            .applyMethod(request.getApplyMethod())
            .applyInfo(request.getApplyInfo())
            .projectProcessMethod(request.getProjectProcessMethod())
            .recruitmentPositions(request.getRecruitmentPositions())
            .title(request.getTitle())
            .content(request.getContent())
            .build());
    }
}

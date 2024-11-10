package com.prismworks.prism.domain.community.controller;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.community.dto.CommunityDto;
import com.prismworks.prism.domain.community.dto.CommunityDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.community.dto.CommunityDto.CreateRecruitmentPostResponse;
import com.prismworks.prism.domain.community.model.ContactMethod;
import com.prismworks.prism.domain.community.model.ProjectPosition;
import com.prismworks.prism.domain.community.model.ProjectProcessMethod;
import com.prismworks.prism.domain.community.model.RecruitmentStatus;
import com.prismworks.prism.domain.project.dto.MemberDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/team-recruitment/detail/{communityId}")
    public ApiSuccessResponse getRecruitmentPost(@PathVariable String communityId) {

        CommunityDto.RecruitmentPostDetailResponse response = CommunityDto.RecruitmentPostDetailResponse.builder()
            .recruitmentStatus(RecruitmentStatus.RECRUITING)
            .title("스위그 Prism 모집중")
            .writer("눈꽃")
            .viewCount(20)
            .projectUrlLink("https://prism-space.vercel.app/")
            .projectDescription("이 프로젝트는 알게 모르게 침투한 영역에 대한 ㅇㅇㅇㅇ")
            .categories(Arrays.asList("금융", "생산성"))
            .skills(Arrays.asList("Spring Framework", "Python"))
            .recruitmentStartAt(LocalDateTime.of(2024, 5, 11, 0, 0))
            .recruitmentEndAt(LocalDateTime.of(2024, 7, 22, 0, 0))
            .projectProcessMethod(ProjectProcessMethod.ONLINE_AND_OFFLINE)
            .recruitmentPositions(Arrays.asList(
                new CommunityDto.RecruitmentPositionItem(ProjectPosition.DESIGNER, 2),
                new CommunityDto.RecruitmentPositionItem(ProjectPosition.PLANNER, 1)
            ))
            .contactMethod(ContactMethod.KAKAO_TALK)
            .contactInfo("카톡아이디")
            .applicationMethod(ContactMethod.GOOGLE_FORM)
            .applicationInfo("구글폼링크")
            .teamMembers(
                Arrays.asList(
                    MemberDto.MemberDetailDto.builder()
                        .userId("아이디")
                        .name("일지영")
                        .email("이메일이래용")
                        .introduction("어떻게 줘야하나")
                        .roles(Arrays.asList("개발자", "기획자"))
                        .strengths(Arrays.asList("배려", "사고력"))
                        .interestDomains(Arrays.asList("금융", "생산성"))
                        .joinsProject(3)
                        .build()
                    ,MemberDto.MemberDetailDto.builder()
                        .userId("아이디")
                        .name("이지영")
                        .email("email")
                        .introduction("3년차/재직중")
                        .roles(Arrays.asList("백엔드", "프론트"))
                        .strengths(Arrays.asList("친화력", "도전정신"))
                        .interestDomains(Arrays.asList("통신", "보안"))
                        .joinsProject(3)
                        .build()
                )
            )
            .build();
        return ApiSuccessResponse.defaultOk(response);
    }
}

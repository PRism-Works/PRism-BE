package com.prismworks.prism.domain.post.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.post.dto.MyPostCommonFilter;
import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostResponse;
import com.prismworks.prism.domain.post.dto.PostDto.GetMyRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.dto.RecruitmentPostCommonFilter;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProjectPosition;
import com.prismworks.prism.domain.post.model.ProjectProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    @PostMapping("/recruitment")
    public ApiSuccessResponse createRecruitmentPost(@RequestBody CreateRecruitmentPostRequest request) {
        return ApiSuccessResponse.defaultOk(CreateRecruitmentPostResponse.builder()
            .recruitStartAt(request.getRecruitStartAt())
            .recruitEndAt(request.getRecruitEndAt())
            .contactMethod(request.getContactMethod())
            .contactInfo(request.getContactInfo())
            .applyMethod(request.getApplyMethod())
            .applyInfo(request.getApplyInfo())
            .projectProcessMethod(request.getProjectProcessMethod())
            .recruitPositions(request.getRecruitPositions())
            .title(request.getTitle())
            .content(request.getContent())
            .build());
    }

    @GetMapping("/recruitment/my")
    public ApiSuccessResponse getMyRecruitmentPosts(
        @RequestParam(required = false) RecruitmentPostCommonFilter recruitmentPostCommonFilter,
        @RequestParam MyPostCommonFilter type) {
        return ApiSuccessResponse.defaultOk(GetMyRecruitmentPostsResponse.builder()
            .postId(1L)
            .positions(List.of(ProjectPosition.values()))
            .title("post title")
            .categories(List.of("category1", "category2"))
            .recruitEndAt(LocalDateTime.now())
            .viewCount(10)
            .build());
    }

    @GetMapping("/recruitment/detail/{postId}")
    public ApiSuccessResponse getRecruitmentPostDetail(@PathVariable int postId) {
        PostDto.RecruitmentPostDetailDto response = PostDto.RecruitmentPostDetailDto.builder()
            .recruitmentStatus(RecruitmentStatus.RECRUITING)
            .title("스위그 Prism 모집중")
            .writer("눈꽃")
            .viewCount(20)
            .projectUrlLink("https://prism-space.vercel.app/")                          //TODO : post 테이블에 추가
            .projectDescription("이 프로젝트는 알게 모르게 침투한 영역에 대한 ㅇㅇㅇㅇ")   //TODO : post 테이블에 추가
            .categories(Arrays.asList("금융", "생산성"))                                     //TODO : post 테이블에 추가
            .skills(Arrays.asList("Spring Framework", "Python"))                                //TODO : post 테이블에 추가
            .recruitmentStartAt(LocalDateTime.of(2024, 5, 11, 0, 0))
            .recruitmentEndAt(LocalDateTime.of(2024, 7, 22, 0, 0))
            .projectProcessMethod(ProjectProcessMethod.ONLINE_AND_OFFLINE)
            .recruitmentPositions(Arrays.asList(
                new PostDto.RecruitPositionItem(ProjectPosition.DESIGNER, 2),
                new PostDto.RecruitPositionItem(ProjectPosition.PLANNER, 1)
            ))
            .contactMethod(ContactMethod.KAKAO_TALK)
            .contactInfo("카톡아이디")
            .applicationMethod(ContactMethod.GOOGLE_FORM)
            .applicationInfo("구글폼링크")
            .build();

        return ApiSuccessResponse.defaultOk(response);
    }
}

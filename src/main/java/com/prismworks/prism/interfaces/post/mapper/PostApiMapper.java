package com.prismworks.prism.interfaces.post.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.prismworks.prism.application.post.dto.param.SearchRecruitmentPostParam;
import com.prismworks.prism.application.post.dto.param.WritePostParam;
import com.prismworks.prism.application.post.dto.result.SearchRecruitmentPostResult;
import com.prismworks.prism.application.post.dto.result.ViewPostResult;
import com.prismworks.prism.application.post.dto.result.WritPostResult;
import com.prismworks.prism.domain.post.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.project.dto.ProjectDetailInfo;
import com.prismworks.prism.domain.user.dto.UserDetailInfo;
import com.prismworks.prism.interfaces.post.dto.request.CreateRecruitmentPostRequest;
import com.prismworks.prism.interfaces.post.dto.request.CreateRecruitmentPostRequest.RecruitPositionItem;
import com.prismworks.prism.interfaces.post.dto.request.SearchBookmarkedRecruitmentPostsRequest;
import com.prismworks.prism.interfaces.post.dto.request.SearchRecruitmentPostsRequest;
import com.prismworks.prism.interfaces.post.dto.response.CreateRecruitmentPostResponse;
import com.prismworks.prism.interfaces.post.dto.response.GetRecruitmentPostDetailResponse;
import com.prismworks.prism.interfaces.post.dto.response.GetRecruitmentPostDetailResponse.CommonProjectItem;
import com.prismworks.prism.interfaces.post.dto.response.GetRecruitmentPostDetailResponse.CommonRecruitmentPostItem;
import com.prismworks.prism.interfaces.post.dto.response.GetRecruitmentPostDetailResponse.RecruitmentPositionItem;
import com.prismworks.prism.interfaces.post.dto.response.GetRecruitmentPostDetailResponse.WriterInfo;
import com.prismworks.prism.interfaces.post.dto.response.SearchRecruitmentPostsResponse;
import com.prismworks.prism.interfaces.post.dto.response.SearchRecruitmentPostsResponse.SearchRecruitmentPostItem;

@Component
public class PostApiMapper {

    public WritePostParam fromCreateRecruitmentPostRequest(CreateRecruitmentPostRequest request,
        String userId
    ) {
        return WritePostParam.builder()
            .userId(userId)
            .openUntilRecruited(request.isOpenUntilRecruited())
            .recruitmentStartAt(request.getRecruitmentStartAt())
            .recruitmentEndAt(request.getRecruitmentEndAt())
            .title(request.getTitle())
            .content(request.getContent())
            .projectId(request.getProjectId())
            .contactMethod(request.getContactMethod())
            .contactInfo(request.getContactInfo())
            .applyMethod(request.getApplyMethod())
            .applyInfo(request.getApplyInfo())
            .processMethod(request.getProcessMethod())
            .recruitPositions(request.getRecruitPositions().stream()
                .map(this::toRecruitPositionItem)
                .collect(Collectors.toList()))
            .build();
    }

    public CreateRecruitmentPostResponse toCreateRecruitmentPostResponse(
        WritPostResult result
    ) {
        PostRecruitmentInfo postRecruitmentInfo = result.getPostRecruitmentInfo();

        return CreateRecruitmentPostResponse.builder()
            .postId(postRecruitmentInfo.getPostId())
            .build();
    }

    public SearchRecruitmentPostParam fromSearchRecruitmentPostRequest(
        SearchRecruitmentPostsRequest request, String userId
    ) {
        return SearchRecruitmentPostParam.builder()
            .userId(userId)
            .recruitmentPositions(request.getRecruitmentPositions())
            .categoryIds(request.getCategoryIds())
            .processMethod(request.getProcessMethod())
            .skills(request.getSkills())
            .recruitmentStatuses(request.isRecruiting() ? List.of(RecruitmentStatus.RECRUITING)
                : List.of(RecruitmentStatus.RECRUITING, RecruitmentStatus.CLOSED))
            .isBookmarkSearch(false)
            .pageNo(request.getPageNo())
            .pageSize(request.getPageSize())
            .sort(request.getSort())
            .build();
    }

    public SearchRecruitmentPostParam fromSearchBookmarkedPostRequest(
        SearchBookmarkedRecruitmentPostsRequest request, String userId
    ) {
        return SearchRecruitmentPostParam.builder()
            .userId(userId)
            .recruitmentPositions(request.getRecruitmentPositions())
            .categoryIds(request.getCategoryIds())
            .processMethod(request.getProcessMethod())
            .skills(request.getSkills())
            .recruitmentStatuses(request.isRecruiting() ? List.of(RecruitmentStatus.RECRUITING)
                : List.of(RecruitmentStatus.RECRUITING, RecruitmentStatus.CLOSED))
            .isBookmarkSearch(true)
            .pageNo(request.getPageNo())
            .pageSize(request.getPageSize())
            .sort(request.getSort())
            .build();
    }

    public SearchRecruitmentPostsResponse toSearchPostsResponse(
        SearchRecruitmentPostResult result
    ) {
        Page<SearchRecruitmentPostInfo> searchResult = result.getSearchRecruitmentPostInfos();
        return SearchRecruitmentPostsResponse.builder()
            .totalCount(searchResult.getTotalElements())
            .totalPages(searchResult.getTotalPages())
            .currentPage(searchResult.getNumber())
            .posts(searchResult.getContent().stream()
                .map(SearchRecruitmentPostItem::new)
                .collect(Collectors.toList()))
            .build();
    }

    public GetRecruitmentPostDetailResponse toGetRecruitmentPostDetailResponse(ViewPostResult result) {
        UserDetailInfo writerInfo = result.getWriterInfo();
        PostRecruitmentInfo postRecruitmentInfo = result.getPostRecruitmentInfo();
        ProjectDetailInfo projectInfo = result.getProjectInfo();

        WriterInfo writer = WriterInfo.builder()
            .userId(writerInfo.getUserId())
            .username(writerInfo.getUsername())
            .email(writerInfo.getEmail())
            .build();

        CommonRecruitmentPostItem recruitmentPostItem = CommonRecruitmentPostItem.builder()
            .postId(postRecruitmentInfo.getPostId())
            .title(postRecruitmentInfo.getTitle())
            .content(postRecruitmentInfo.getContent())
            .writer(writer)
            .viewCount(postRecruitmentInfo.getViewCount())
            .createdAt(postRecruitmentInfo.getCreatedAt())
            .recruitmentStartAt(postRecruitmentInfo.getRecruitmentStartAt())
            .recruitmentEndAt(postRecruitmentInfo.getRecruitmentEndAt())
            .processMethod(postRecruitmentInfo.getProcessMethod())
            .contactMethod(postRecruitmentInfo.getContactMethod())
            .contactInfo(postRecruitmentInfo.getContactInfo())
            .applyMethod(postRecruitmentInfo.getApplyMethod())
            .applyInfo(postRecruitmentInfo.getApplyInfo())
            .recruitmentStatus(postRecruitmentInfo.getRecruitmentStatus())
            .recruitmentPositions(postRecruitmentInfo.getRecruitmentPositions().stream()
                .map(position -> new RecruitmentPositionItem(
                    position.getPositionId(),
                    position.getPosition().getValue(),
                    position.getRecruitmentCount()))
                .collect(Collectors.toSet()))
            .build();

        CommonProjectItem projectItem = CommonProjectItem.builder()
            .projectId(projectInfo.getProjectId())
            .projectName(projectInfo.getProjectName())
            .organizationName(projectInfo.getOrganizationName())
            .startDate(projectInfo.getStartDate())
            .endDate(projectInfo.getEndDate())
            .projectUrlLink(projectInfo.getProjectUrlLink())
            .urlVisibility(projectInfo.isUrlVisibility())
            .projectDescription(projectInfo.getProjectDescription())
            .categories(projectInfo.getCategories())
            .skills(projectInfo.getSkills())
            .build();

        return new GetRecruitmentPostDetailResponse(recruitmentPostItem, projectItem);
    }

    private WritePostParam.RecruitPositionItem toRecruitPositionItem(RecruitPositionItem item) {
        return WritePostParam.RecruitPositionItem.builder()
            .position(item.getPosition())
            .recruitmentCount(item.getCount())
            .build();
    }
}

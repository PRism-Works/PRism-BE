package com.prismworks.prism.domain.post.interfaces.mapper;

import com.prismworks.prism.domain.post.application.dto.param.WritePostParam;
import com.prismworks.prism.domain.post.application.dto.result.ViewPostResult;
import com.prismworks.prism.domain.post.domain.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchRecruitmentPostItem;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.interfaces.dto.response.PostResponse.CommonProjectItem;
import com.prismworks.prism.domain.post.interfaces.dto.response.PostResponse.CommonRecruitmentPostItem;
import com.prismworks.prism.domain.post.interfaces.dto.response.PostResponse.GetRecruitmentPostDetailResponse;
import com.prismworks.prism.domain.post.interfaces.dto.response.PostResponse.RecruitmentPositionItem;
import com.prismworks.prism.domain.post.interfaces.dto.response.PostResponse.WriterInfo;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.domain.user.dto.UserDto.UserProfileDetail;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PostApiMapper {

    public WritePostParam toWritePostParam(CreateRecruitmentPostRequest request, String userId) {
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

    private WritePostParam.RecruitPositionItem toRecruitPositionItem(PostDto.RecruitPositionItem item) {
        return WritePostParam.RecruitPositionItem.builder()
            .position(item.getPosition())
            .recruitmentCount(item.getCount())
            .build();
    }

    public SearchRecruitmentPostsResponse toSearchPostsResponse(
        Page<SearchRecruitmentPostInfo> searchResult
    ) {
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
        UserProfileDetail writerInfo = result.getWriterInfo();
        PostRecruitmentInfo postRecruitmentInfo = result.getPostRecruitmentInfo();
        ProjectDetailDto projectInfo = result.getProjectInfo();

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
}

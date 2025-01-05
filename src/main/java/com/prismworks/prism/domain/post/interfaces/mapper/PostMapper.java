package com.prismworks.prism.domain.post.interfaces.mapper;

import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchRecruitmentPostItem;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.domain.model.RecruitmentPostInfo;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public SearchRecruitmentPostsResponse toSearchPostsResponse(
        Page<RecruitmentPostInfo> searchResult
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
}

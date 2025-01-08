package com.prismworks.prism.domain.post.application.dto.result;

import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@RequiredArgsConstructor
public class SearchRecruitmentPostResult {
    private final Page<SearchRecruitmentPostInfo> searchRecruitmentPostInfos;
}

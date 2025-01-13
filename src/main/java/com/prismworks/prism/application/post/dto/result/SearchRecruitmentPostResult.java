package com.prismworks.prism.application.post.dto.result;

import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@RequiredArgsConstructor
public class SearchRecruitmentPostResult {
    private final Page<SearchRecruitmentPostInfo> searchRecruitmentPostInfos;
}

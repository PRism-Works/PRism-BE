package com.prismworks.prism.domain.post.domain.repository.custom;

import com.prismworks.prism.domain.post.domain.dto.query.GetRecruitmentPostsQuery;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import org.springframework.data.domain.Page;

public interface PostTeamRecruitmentCustomRepository {

    Page<SearchRecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPostsQuery condition);
}

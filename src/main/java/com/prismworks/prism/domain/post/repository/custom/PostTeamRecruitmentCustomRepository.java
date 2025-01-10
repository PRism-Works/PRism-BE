package com.prismworks.prism.domain.post.repository.custom;

import com.prismworks.prism.domain.post.dto.query.GetRecruitmentPostsQuery;
import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import org.springframework.data.domain.Page;

public interface PostTeamRecruitmentCustomRepository {

    Page<SearchRecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPostsQuery condition);
}

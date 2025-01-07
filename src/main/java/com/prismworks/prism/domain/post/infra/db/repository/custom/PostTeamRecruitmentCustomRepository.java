package com.prismworks.prism.domain.post.infra.db.repository.custom;

import com.prismworks.prism.domain.post.domain.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import org.springframework.data.domain.Page;

public interface PostTeamRecruitmentCustomRepository {

    Page<SearchRecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPosts condition);
}

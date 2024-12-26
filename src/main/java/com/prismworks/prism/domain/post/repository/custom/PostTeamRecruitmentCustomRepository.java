package com.prismworks.prism.domain.post.repository.custom;

import com.prismworks.prism.domain.post.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import org.springframework.data.domain.Page;

public interface PostTeamRecruitmentCustomRepository {

    Page<RecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPosts condition);
}

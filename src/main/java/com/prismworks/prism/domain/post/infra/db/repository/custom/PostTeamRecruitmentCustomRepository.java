package com.prismworks.prism.domain.post.infra.db.repository.custom;

import com.prismworks.prism.domain.post.application.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.domain.model.RecruitmentPostInfo;
import org.springframework.data.domain.Page;

public interface PostTeamRecruitmentCustomRepository {

    Page<RecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPosts condition);
}

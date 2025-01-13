package com.prismworks.prism.domain.post.repository;

import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.dto.query.GetRecruitmentPostsQuery;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PostRepository {
    PostTeamRecruitment savePostTeamRecruitment(PostTeamRecruitment postTeamRecruitment);

    Page<SearchRecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPostsQuery query);

    Optional<PostTeamRecruitment> getPostTeamRecruitment(Long postId);

    int incrementViewCount(Long postId);
}

package com.prismworks.prism.infrastructure.db.post.impl;

import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.dto.query.GetRecruitmentPostsQuery;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.repository.PostRepository;
import com.prismworks.prism.infrastructure.db.post.PostJpaRepository;
import com.prismworks.prism.infrastructure.db.post.PostTeamRecruitmentJpaRepository;
import com.prismworks.prism.infrastructure.db.post.custom.PostTeamRecruitmentCustomRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;
    private final PostTeamRecruitmentJpaRepository postTeamRecruitmentJpaRepository;
    private final PostTeamRecruitmentCustomRepository postTeamRecruitmentCustomRepository;

    @Override
    public PostTeamRecruitment savePostTeamRecruitment(PostTeamRecruitment postTeamRecruitment) {
        return postTeamRecruitmentJpaRepository.save(postTeamRecruitment);
    }

    @Override
    public Page<SearchRecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPostsQuery query) {
        return postTeamRecruitmentCustomRepository.searchRecruitmentPosts(query);
    }

    @Override
    public Optional<PostTeamRecruitment> getPostTeamRecruitment(Long postId) {
        return postTeamRecruitmentJpaRepository.findByPostId(postId);
    }

    @Override
    public int incrementViewCount(Long postId) {
        return postJpaRepository.incrementViewCountById(postId);
    }
}

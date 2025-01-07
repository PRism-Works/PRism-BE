package com.prismworks.prism.domain.post.domain.service;

import com.prismworks.prism.domain.post.domain.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.domain.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.domain.dto.command.CreateRecruitmentPostCommand;
import com.prismworks.prism.domain.post.domain.model.Post;
import com.prismworks.prism.domain.post.domain.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.infra.db.repository.PostRepository;
import com.prismworks.prism.domain.post.infra.db.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.infra.db.repository.TeamRecruitmentPositionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository postRecruitmentRepository;
	private final TeamRecruitmentPositionRepository teamRecruitmentPositionRepository;

	@Transactional
	public PostRecruitmentInfo createRecruitmentPost(CreateRecruitmentPostCommand command) {
		PostTeamRecruitment postTeamRecruitment = new PostTeamRecruitment(command);
		PostTeamRecruitment savedPostTeamRecruitment = postRecruitmentRepository.save(postTeamRecruitment);

		return new PostRecruitmentInfo(savedPostTeamRecruitment);
	}

	@Transactional(readOnly = true)
	public Page<SearchRecruitmentPostInfo> searchRecruitmentPost(GetRecruitmentPosts query) {
		return postRecruitmentRepository.searchRecruitmentPosts(query);
	}

    @Transactional
    public PostRecruitmentInfo getRecruitmentDetail(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found for ID: " + postId));

        PostTeamRecruitment recruitment = postRecruitmentRepository.findByPost(post)
            .orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));

		Hibernate.initialize(recruitment.getRecruitmentPositions());

        return new PostRecruitmentInfo(recruitment);
    }

	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCountById(postId);
	}
}

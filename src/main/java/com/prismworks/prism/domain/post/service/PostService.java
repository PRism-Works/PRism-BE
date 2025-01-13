package com.prismworks.prism.domain.post.service;

import com.prismworks.prism.domain.post.dto.query.GetRecruitmentPostsQuery;
import com.prismworks.prism.domain.post.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.dto.command.CreateRecruitmentPostCommand;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.repository.PostRepository;
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

	@Transactional
	public PostRecruitmentInfo createRecruitmentPost(CreateRecruitmentPostCommand command) {
		PostTeamRecruitment postTeamRecruitment = new PostTeamRecruitment(command);
		PostTeamRecruitment savedPostTeamRecruitment =
			postRepository.savePostTeamRecruitment(postTeamRecruitment);

		return new PostRecruitmentInfo(savedPostTeamRecruitment);
	}

	@Transactional(readOnly = true)
	public Page<SearchRecruitmentPostInfo> searchRecruitmentPost(GetRecruitmentPostsQuery query) {
		return postRepository.searchRecruitmentPosts(query);
	}

    @Transactional
    public PostRecruitmentInfo getRecruitmentDetail(Long postId) {
        PostTeamRecruitment recruitment = postRepository.getPostTeamRecruitment(postId)
            .orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));

		Hibernate.initialize(recruitment.getRecruitmentPositions());

        return new PostRecruitmentInfo(recruitment);
    }

	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCount(postId);
	}
}

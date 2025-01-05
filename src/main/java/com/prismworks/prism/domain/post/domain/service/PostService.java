package com.prismworks.prism.domain.post.domain.service;

import com.prismworks.prism.domain.post.interfaces.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.RecruitmentPostDetailDto;
import com.prismworks.prism.domain.post.application.dto.command.PostCommand.CreatePost;
import com.prismworks.prism.domain.post.application.dto.command.PostTeamRecruitmentCommand.CreatePostTeamRecruitment;
import com.prismworks.prism.domain.post.application.dto.command.TeamRecruitmentPositionCommand.CreateTeamRecruitmentPosition;
import com.prismworks.prism.domain.post.application.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.domain.model.Post;
import com.prismworks.prism.domain.post.domain.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.domain.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.domain.model.TeamRecruitmentPosition;
import com.prismworks.prism.domain.post.infra.db.repository.PostRepository;
import com.prismworks.prism.domain.post.infra.db.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.infra.db.repository.TeamRecruitmentPositionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository postRecruitmentRepository;
	private final TeamRecruitmentPositionRepository teamRecruitmentPositionRepository;

	@Transactional
	public PostRecruitmentInfo createRecruitmentPost(CreateRecruitmentPostRequest req, String userId) {
		CreatePost createPostCommand = req.toCreatePostCommand(userId);
		Post post = postRepository.save(new Post(createPostCommand));

		List<CreateTeamRecruitmentPosition> createTeamRecruitmentPositionCommand =
			req.toCreateTeamRecruitmentPositionCommand();
		Set<TeamRecruitmentPosition> recruitmentPositions = createTeamRecruitmentPositionCommand.stream()
			.map(TeamRecruitmentPosition::new)
			.collect(Collectors.toSet());

		CreatePostTeamRecruitment createRecruitmentPostCommand =
			req.toCreatePostTeamRecruitmentCommand(post, recruitmentPositions);
		PostTeamRecruitment postTeamRecruitment =
			postRecruitmentRepository.save(new PostTeamRecruitment(createRecruitmentPostCommand));

		return new PostRecruitmentInfo(post, postTeamRecruitment, recruitmentPositions);
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

        return new PostRecruitmentInfo(post, recruitment, recruitment.getRecruitmentPositions());
    }

	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCountById(postId);
	}
}

package com.prismworks.prism.domain.post.service;

import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.dto.PostDto.RecruitmentPostDetailDto;
import com.prismworks.prism.domain.post.dto.command.PostCommand.CreatePost;
import com.prismworks.prism.domain.post.dto.command.PostTeamRecruitmentCommand.CreatePostTeamRecruitment;
import com.prismworks.prism.domain.post.dto.command.TeamRecruitmentPositionCommand.CreateTeamRecruitmentPosition;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import com.prismworks.prism.domain.post.model.UserPostBookmark;
import com.prismworks.prism.domain.post.repository.PostRepository;
import com.prismworks.prism.domain.post.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.repository.TeamRecruitmentPositionRepository;
import com.prismworks.prism.domain.post.repository.UserPostBookmarkRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository postRecruitmentRepository;
	private final TeamRecruitmentPositionRepository teamRecruitmentPositionRepository;
	private final UserPostBookmarkRepository userPostBookmarkRepository;

	@Transactional
	public PostRecruitmentInfo createRecruitmentPost(CreateRecruitmentPostRequest req, String userId) {
		CreatePost createPostCommand = req.toCreatePostCommand(userId);
		Post post = postRepository.save(new Post(createPostCommand));

		List<CreateTeamRecruitmentPosition> createTeamRecruitmentPositionCommand =
			req.toCreateTeamRecruitmentPositionCommand();
		List<TeamRecruitmentPosition> recruitmentPositions = createTeamRecruitmentPositionCommand.stream()
			.map(TeamRecruitmentPosition::new)
			.toList();

		CreatePostTeamRecruitment createRecruitmentPostCommand =
			req.toCreatePostTeamRecruitmentCommand(post, recruitmentPositions);
		PostTeamRecruitment postTeamRecruitment =
			postRecruitmentRepository.save(new PostTeamRecruitment(createRecruitmentPostCommand));

		return new PostRecruitmentInfo(post, postTeamRecruitment, recruitmentPositions);
	}

    @Transactional
    public RecruitmentPostDetailDto getRecruitmentDetail(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found for ID: " + postId));

        PostTeamRecruitment recruitment = postRecruitmentRepository.findByPost(post)
            .orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));

		Hibernate.initialize(recruitment.getRecruitmentPositions());

        return RecruitmentPostDetailDto.of(
            post,
            recruitment
        );
    }

	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCountById(postId);
	}

	public UserPostBookmark bookmark(String userId, Long postId) {
		Optional<UserPostBookmark> bookmark = userPostBookmarkRepository.findByUserIdAndPostId(userId, postId);

		if (bookmark.isPresent()) {
			UserPostBookmark existingBookmark = bookmark.get();
			existingBookmark.setActiveFlag(!existingBookmark.isActiveFlag());
			return userPostBookmarkRepository.save(existingBookmark);
		} else {
			UserPostBookmark newBookmark = new UserPostBookmark();
			newBookmark.setUserId(userId);
			newBookmark.setPostId(postId);
			userPostBookmarkRepository.save(newBookmark);
			return newBookmark;
		}
	}
}

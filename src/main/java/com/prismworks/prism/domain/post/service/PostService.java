package com.prismworks.prism.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prismworks.prism.domain.post.dto.PostDto.RecruitmentPostDetailDto;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import com.prismworks.prism.domain.post.repository.PostRepository;
import com.prismworks.prism.domain.post.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.repository.TeamRecruitmentPositionRepository;
import com.prismworks.prism.domain.user.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository recruitmentRepository;
	private final TeamRecruitmentPositionRepository teamRecruitmentPositionRepository;
	private final UserService userService;

	@Transactional
	public RecruitmentPostDetailDto getRecruitmentDetail(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("Post not found for ID: " + postId));

		PostTeamRecruitment recruitment = recruitmentRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));

		List<TeamRecruitmentPosition> recruitmentPositions = teamRecruitmentPositionRepository.findByPostTeamRecruitmentId(
			recruitment.getPostTeamRecruitmentId());

		return RecruitmentPostDetailDto.of(
			post,
			recruitment,
			userService.getUserProfileDetail(post.getUserId()),
			recruitmentPositions
		);
	}

	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCountById(postId);
	}
}

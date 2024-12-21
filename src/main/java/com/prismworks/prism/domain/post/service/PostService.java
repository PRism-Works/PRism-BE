package com.prismworks.prism.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prismworks.prism.domain.post.dto.PostDto.RecruitmentPostDetailDto;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.repository.PostRepository;
import com.prismworks.prism.domain.post.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.repository.RecruitmentPositionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository recruitmentRepository;
	private final RecruitmentPositionRepository recruitmentPositionRepository;

	public PostService(PostRepository postRepository, PostTeamRecruitmentRepository recruitmentRepository, RecruitmentPositionRepository recruitmentPositionRepository) {
		this.postRepository = postRepository;
		this.recruitmentRepository = recruitmentRepository;
		this.recruitmentPositionRepository = recruitmentPositionRepository;
	}

	@Transactional
	public RecruitmentPostDetailDto getRecruitmentDetail(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("Post not found for ID: " + postId));

		PostTeamRecruitment recruitment = recruitmentRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));

		List<RecruitmentPosition> recruitmentPositions = recruitmentPositionRepository.findByPostTeamRecruitmentId(
			recruitment.getPostTeamRecruitmentId());

		return RecruitmentPostDetailDto.of(post, recruitment, recruitmentPositions);
	}
}

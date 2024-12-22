package com.prismworks.prism.domain.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prismworks.prism.domain.post.dto.PostDto.RecruitmentPostDetailDto;
import com.prismworks.prism.domain.post.repository.PostRepository;
import com.prismworks.prism.domain.post.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository recruitmentRepository;

	public PostService(PostRepository postRepository, PostTeamRecruitmentRepository recruitmentRepository) {
		this.postRepository = postRepository;
		this.recruitmentRepository = recruitmentRepository;
	}

//	@Transactional
//	public RecruitmentPostDetailDto getRecruitmentDetail(Long postId) {
//		Post post = postRepository.findById(postId)
//			.orElseThrow(() -> new EntityNotFoundException("Post not found for ID: " + postId));
//
//		PostTeamRecruitment recruitment = recruitmentRepository.findById(postId)
//			.orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));
//
//		return RecruitmentPostDetailDto.of(post, recruitment);
//	}
}

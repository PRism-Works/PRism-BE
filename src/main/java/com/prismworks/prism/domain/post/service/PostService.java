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
import com.prismworks.prism.domain.post.repository.PostRepository;
import com.prismworks.prism.domain.post.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.post.repository.TeamRecruitmentPositionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final PostTeamRecruitmentRepository recruitmentRepository;
	private final TeamRecruitmentPositionRepository teamRecruitmentPositionRepository;

	@Transactional
	public PostRecruitmentInfo createRecruitmentPost(CreateRecruitmentPostRequest req, String userId) {
		CreatePost createPostCommand = req.toCreatePostCommand(userId);
		Post post = postRepository.save(new Post(createPostCommand));

		CreatePostTeamRecruitment createRecruitmentPostCommand =
			req.toCreatePostTeamRecruitmentCommand(post.getPostId());
		PostTeamRecruitment postTeamRecruitment =
			recruitmentRepository.save(new PostTeamRecruitment(createRecruitmentPostCommand));

		List<CreateTeamRecruitmentPosition> createTeamRecruitmentPositionCommand =
			req.toCreateTeamRecruitmentPositionCommand(postTeamRecruitment.getPostTeamRecruitmentId());
		List<TeamRecruitmentPosition> positions = createTeamRecruitmentPositionCommand.stream()
			.map(TeamRecruitmentPosition::new)
			.toList();
		teamRecruitmentPositionRepository.saveAll(positions);

		return new PostRecruitmentInfo(post, postTeamRecruitment, positions);
	}

    @Transactional
    public RecruitmentPostDetailDto getRecruitmentDetail(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found for ID: " + postId));

        PostTeamRecruitment recruitment = recruitmentRepository.findByPostId(postId)
            .orElseThrow(() -> new EntityNotFoundException("Recruitment not found for Post ID: " + postId));

        List<TeamRecruitmentPosition> recruitmentPositions = teamRecruitmentPositionRepository.findByPostTeamRecruitmentId(
            recruitment.getPostTeamRecruitmentId());

        return RecruitmentPostDetailDto.of(
            post,
            recruitment,
            recruitmentPositions
        );
    }

	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCountById(postId);
	}
}

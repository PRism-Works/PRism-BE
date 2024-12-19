package com.prismworks.prism.domain.post.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.prismworks.prism.domain.post.repository.PostRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class PostViewCountAspect {

	private final PostRepository postRepository;

	@Before("execution(* com.prismworks.prism.domain.post.service.PostService.get*Detail(..)) && args(postId, ..)")
	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCountById(postId);
	}
}

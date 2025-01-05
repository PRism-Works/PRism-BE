package com.prismworks.prism.domain.post.aspect;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.prismworks.prism.domain.post.domain.service.PostService;

@Aspect
@Component
@RequiredArgsConstructor
public class PostViewCountAspect {

	private final PostService postService;

	@After("execution(* com.prismworks.prism.domain.post.application.PostFacade.viewPost(..)) && args(postId,..)")
	public void incrementViewCount(Long postId) {
		postService.incrementViewCount(postId);
	}
}

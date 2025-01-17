package com.prismworks.prism.application.post.aspect;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.prismworks.prism.domain.post.service.PostService;

@Aspect
@Component
@RequiredArgsConstructor
public class PostViewCountAspect {

	private final PostService postService;

	@After("execution(* com.prismworks.prism.application.post.PostFacade.viewPost(..)) && args(postId,..)")
	public void incrementViewCount(Long postId) {
		postService.incrementViewCount(postId);
	}
}

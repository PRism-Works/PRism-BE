package com.prismworks.prism.domain.post.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.QPost;
import com.prismworks.prism.domain.post.model.QPostTeamRecruitment;
import com.prismworks.prism.domain.post.model.QTeamRecruitmentPosition;
import com.prismworks.prism.domain.post.model.QUserPostBookmark;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.project.model.QProject;
import com.prismworks.prism.domain.search.dto.SearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostTeamRecruitmentRepositoryImpl implements PostTeamRecruitmentRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<SearchDto.PostTeamRecruitmentSearchResponse> findBookmarkedPostTeamRecruitments(
		String userId,
		Set<String> positions,
		List<String> categories,
		List<String> skills,
		ProcessMethod processMethod,
		RecruitmentStatus recruitmentStatus,
		Pageable pageable)
	{
		QUserPostBookmark userPostBookmark = QUserPostBookmark.userPostBookmark;
		QPostTeamRecruitment postTeamRecruitment = QPostTeamRecruitment.postTeamRecruitment;
		QProject project = QProject.project;
		QTeamRecruitmentPosition teamRecruitmentPosition = QTeamRecruitmentPosition.teamRecruitmentPosition;
		QPost post = QPost.post;

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(userPostBookmark.userId.eq(userId))
			.and(userPostBookmark.activeFlag.isTrue());

		if (positions != null && !positions.isEmpty()) {
			builder.and(teamRecruitmentPosition.position.stringValue().in(positions));
		}
		if (categories != null && !categories.isEmpty()) {
			builder.and(project.categories.any().category.name.in(categories));
		}
		if (skills != null && !skills.isEmpty()) {
			builder.and(project.skills.any().in(skills));
		}
		if (processMethod != null) {
			builder.and(postTeamRecruitment.processMethod.eq(processMethod));
		}
		if (recruitmentStatus != null) {
			builder.and(postTeamRecruitment.recruitmentStatus.eq(recruitmentStatus));
		}

		List<SearchDto.PostTeamRecruitmentSearchResponse> content = queryFactory
			.select(Projections.constructor(SearchDto.PostTeamRecruitmentSearchResponse.class,
				project,
				postTeamRecruitment.processMethod,
				postTeamRecruitment.recruitmentStatus)
			)
			.from(userPostBookmark)
			.innerJoin(post).on(userPostBookmark.postId.eq(post.postId))
			.innerJoin(postTeamRecruitment).on(post.postId.eq(postTeamRecruitment.post.postId))
			.innerJoin(project).on(postTeamRecruitment.projectId.eq(project.projectId))
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(
			queryFactory
			.select(userPostBookmark.count())
			.from(userPostBookmark)
			.where(builder)
			.fetchOne()
		)
		.orElse(0L);

		return PageableExecutionUtils.getPage(content, pageable, () -> total);
	}
}

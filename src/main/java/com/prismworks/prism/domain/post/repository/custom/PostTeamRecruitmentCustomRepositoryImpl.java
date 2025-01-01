package com.prismworks.prism.domain.post.repository.custom;

import static com.prismworks.prism.domain.post.model.QPostTeamRecruitment.postTeamRecruitment;
import static com.prismworks.prism.domain.post.model.QTeamRecruitmentPosition.teamRecruitmentPosition;
import static com.prismworks.prism.domain.post.model.QUserPostBookmark.userPostBookmark;
import static com.prismworks.prism.domain.project.model.QProject.project;
import static com.prismworks.prism.domain.user.model.QUsers.users;

import com.prismworks.prism.domain.post.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.post.repository.custom.projection.GetPostRecruitmentsProjection;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Repository
public class PostTeamRecruitmentCustomRepositoryImpl implements PostTeamRecruitmentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RecruitmentPostInfo> searchRecruitmentPosts(GetRecruitmentPosts condition) {
        JPAQuery<?> baseQuery = this.generateCommonSearchQuery(condition);

        JPAQuery<Long> countQuery = baseQuery.clone()
            .select(postTeamRecruitment.count());

        boolean isBookmarkSearch = condition.isBookmarkSearch();
        List<GetPostRecruitmentsProjection> postTeamRecruitments = baseQuery.clone()
            .select(Projections.constructor(GetPostRecruitmentsProjection.class,
                postTeamRecruitment,
                project,
                users.as("user"),
                Expressions.as(
                    isBookmarkSearch ? Expressions.constant(true) : this.isUserBookmarkExistsQuery(condition.getUserId()),
                    "isBookmarked"
                )
            ))
            .offset(condition.getPageNo())
            .limit(condition.getPageSize())
            .orderBy(postTeamRecruitment.createdAt.desc())
            .fetch();

        List<RecruitmentPostInfo> contents = postTeamRecruitments.stream()
            .map(GetPostRecruitmentsProjection::toRecruitmentPostInfo)
            .toList();

        PageRequest pageRequest = PageRequest.of(condition.getPageNo(), condition.getPageSize(),
            Direction.DESC, "createdAt");
        return PageableExecutionUtils.getPage(contents, pageRequest, countQuery::fetchOne);
    }

    private JPAQuery<?> generateCommonSearchQuery(GetRecruitmentPosts condition) {
        JPAQuery<?> query = queryFactory
            .from(postTeamRecruitment)
            .join(project).on(postTeamRecruitment.projectId.eq(project.projectId))
            .join(users).on(postTeamRecruitment.post.userId.eq(users.userId))
            .where(
                this.processMethodEq(condition.getProcessMethod()),
                this.projectCategoryIdsIn(condition.getCategoryIds()),
                this.projectSkillsIn(condition.getSkills()),
                this.recruitmentStatusIn(condition.getRecruitmentStatuses()),
                this.recruitmentPositionsIn(condition.getRecruitmentPositions())
            );

        if(condition.isBookmarkSearch()) {
            return query.join(userPostBookmark)
                .on(postTeamRecruitment.post.postId.eq(userPostBookmark.postId))
                .where(userPostBookmark.userId.eq(condition.getUserId())
                    .and(userPostBookmark.activeFlag.eq(true)));
        }

        return query;
    }

    private Expression<Boolean> isUserBookmarkExistsQuery(String userId) {
        return JPAExpressions.selectOne()
            .from(userPostBookmark)
            .where(userPostBookmark.userId.eq(userId)
                .and(userPostBookmark.postId.eq(postTeamRecruitment.post.postId))
                .and(userPostBookmark.activeFlag.eq(true)))
            .exists();
    }

    private BooleanExpression recruitmentPositionsIn(List<RecruitmentPosition> positions) {
        List<Long> postTeamRecruitmentIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(positions)) {
            postTeamRecruitmentIds = queryFactory
                .select(teamRecruitmentPosition.postTeamRecruitment.postTeamRecruitmentId)
                .from(teamRecruitmentPosition)
                .where(teamRecruitmentPosition.position.in(positions))
                .fetch();
        }

        return CollectionUtils.isEmpty(postTeamRecruitmentIds) ? null
            : postTeamRecruitment.postTeamRecruitmentId.in(postTeamRecruitmentIds);
    }

    private BooleanExpression processMethodEq(ProcessMethod processMethod) {
        return Objects.isNull(processMethod) ? null : postTeamRecruitment.processMethod.eq(processMethod);
    }

    private BooleanExpression projectCategoryIdsIn(List<Integer> categoryIds) {

        return CollectionUtils.isEmpty(categoryIds) ? null
            : project.categories.any().category.categoryId.in(categoryIds);
    }

    private BooleanExpression projectSkillsIn(List<String> skills) {
        return CollectionUtils.isEmpty(skills) ? null : project.skills.any().in(skills);
    }

    private BooleanExpression recruitmentStatusIn(List<RecruitmentStatus> statuses) {
        return CollectionUtils.isEmpty(statuses) ? null : postTeamRecruitment.recruitmentStatus.in(statuses);
    }
}

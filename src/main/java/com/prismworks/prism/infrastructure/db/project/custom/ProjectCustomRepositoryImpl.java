package com.prismworks.prism.infrastructure.db.project.custom;

import com.prismworks.prism.infrastructure.db.project.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.interfaces.search.dto.ProjectSearchCondition;
import com.prismworks.prism.interfaces.search.dto.ProjectSearchCondition.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.prismworks.prism.domain.project.model.QProject.project;
import static com.prismworks.prism.domain.project.model.QProjectCategoryJoin.projectCategoryJoin;
import static com.prismworks.prism.domain.project.model.QProjectUserJoin.projectUserJoin;

@RequiredArgsConstructor
@Repository
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProjectProjection.ProjectSearchResult> searchByCondition(ProjectSearchCondition condition, Pageable pageable) { //todo: N+1 해결
        JPAQuery<?> query = this.searchProjectQuery(condition);
        JPAQuery<Long> countQuery = query.clone().select(project.countDistinct());
        List<Project> projects = query.clone()
                .select(project).distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(project.endDate.desc())
                .fetch();

        List<ProjectProjection.ProjectSearchResult> contents = projects.stream().map(project -> ProjectProjection.ProjectSearchResult.builder()
                        .projectId(project.getProjectId())
                        .projectName(project.getProjectName())
                        .organizationName(project.getOrganizationName())
                        .categories(
                                project.getCategories().stream()
                                        .map(projectCategoryJoin -> projectCategoryJoin.getCategory().getName())
                                        .collect(Collectors.toList())
                        )
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .build())
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Long countUserByProjectId(Integer projectId) {
        return queryFactory
                .select(projectUserJoin.count())
                .from(projectUserJoin)
                .where(projectUserJoin.project.projectId.eq(projectId))
                .fetchOne();
    }

    @Override
    public List<ProjectUserJoin> findAllMemberByProjectId(Integer projectId) {
        return queryFactory
                .selectFrom(projectUserJoin)
                .where(this.memberProjectIdEq(projectId))
                .fetch();
    }

    @Override
    public Optional<ProjectUserJoin> findMemberByProjectIdAndEmail(Integer projectId, String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(projectUserJoin)
                        .where(
                                this.memberProjectIdEq(projectId),
                                this.memberEmailEq(email)
                        )
                        .fetchOne()
        );
    }

    private JPAQuery<?> searchProjectQuery(ProjectSearchCondition condition) {
        return queryFactory
                .from(project)
                .innerJoin(projectUserJoin)
                .on(project.eq(projectUserJoin.project))
                .leftJoin(projectCategoryJoin)
                .on(project.eq(projectCategoryJoin.project))
                .where(
                        this.searchWordContains(condition.getSearchType(), condition.getSearchWord()),
                        this.categoryIdIn(condition.getCategories())
                );
    }

    private BooleanExpression searchWordContains(SearchType searchType, String searchWord) {
        if(Objects.isNull(searchType) || !StringUtils.hasText(searchWord)) {
            return null;
        }

        return switch(searchType) {
            case MEMBER_NAME -> projectUserJoin.name.contains(searchWord).or(projectUserJoin.email.contains(searchWord));
            case PROJECT_NAME -> project.projectName.contains(searchWord);
        };
    }

    private BooleanExpression categoryIdIn(Set<Integer> categoryId) {
        if(CollectionUtils.isEmpty(categoryId)) {
            return null;
        }

        return projectCategoryJoin.category.categoryId.in(categoryId);
    }

    private BooleanExpression memberProjectIdEq(Integer projectId) {
        return Objects.isNull(projectId) ? null : projectUserJoin.project.projectId.eq(projectId);
    }

    private BooleanExpression memberEmailEq(String email) {
        return StringUtils.hasText(email) ? projectUserJoin.email.eq(email) : null;
    }
}

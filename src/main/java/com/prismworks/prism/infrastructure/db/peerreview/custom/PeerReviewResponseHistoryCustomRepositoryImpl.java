package com.prismworks.prism.infrastructure.db.peerreview.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.prismworks.prism.domain.peerreview.model.QPeerReviewResponseHistory.*;

@RequiredArgsConstructor
@Repository
public class PeerReviewResponseHistoryCustomRepositoryImpl implements PeerReviewResponseHistoryCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countReviewerByProjectId(Integer projectId) {
        return queryFactory
                .select(peerReviewResponseHistory.reviewerEmail.countDistinct())
                .from(peerReviewResponseHistory)
                .where(this.projectIdEq(projectId))
                .fetchOne();
    }

    private BooleanExpression projectIdEq(Integer projectId) {
        return Objects.isNull(projectId) ? null : peerReviewResponseHistory.projectId.eq(projectId);
    }
}

package com.prismworks.prism.domain.auth.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.prismworks.prism.domain.auth.model.QAuthToken.authToken;

@RequiredArgsConstructor
@Repository
public class AuthTokenCustomRepositoryImpl implements AuthTokenCustomRepository{

    private final JPAQueryFactory queryFactory;

    public void deleteAllByUserId(String userId) {
        queryFactory
                .delete(authToken)
                .where(authToken.userId.eq(userId))
                .execute();
    }
}

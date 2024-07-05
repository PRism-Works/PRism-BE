package com.prismworks.prism.domain.email.repository.custom;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.prismworks.prism.domain.email.model.QEmailAuthCode.emailAuthCode;

@RequiredArgsConstructor
@Repository
public class EmailAuthCodeCustomRepositoryImpl implements EmailAuthCodeCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<EmailAuthCode> findValidCode(String email, AuthType authType, LocalDateTime dateTime) {
        EmailAuthCode authCode = queryFactory
                .selectFrom(emailAuthCode)
                .where(
                        this.emailEq(email),
                        this.authTypeEq(authType),
                        expiredAtGt(dateTime),
                        emailAuthCode.verifiedAt.isNull()
                )
                .fetchOne();

        return Optional.ofNullable(authCode);
    }

    private BooleanExpression emailEq(String email) {
        return StringUtils.hasText(email) ? emailAuthCode.email.eq(email) : null;
    }

    private BooleanExpression authTypeEq(AuthType authType) {
        return authType != null ? emailAuthCode.authType.eq(authType) : null;
    }

    private BooleanExpression expiredAtGt(LocalDateTime dateTime) {
        return Objects.isNull(dateTime) ? null : emailAuthCode.expiredAt.gt(dateTime);
    }
}

package com.prismworks.prism.domain.email.repository.custom;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.prismworks.prism.domain.email.model.QEmailAuthCode.emailAuthCode;

@RequiredArgsConstructor
@Repository
public class EmailAuthCodeCustomRepositoryImpl implements EmailAuthCodeCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmailAuthCode> findAllNotVerifiedCode(String email, AuthType authType, LocalDateTime dateTime) {
        return queryFactory
                .selectFrom(emailAuthCode)
                .where(
                        this.emailEq(email),
                        this.authTypeEq(authType),
                        this.verifiedAtIsNull(),
                        this.expiredAtGt(dateTime)
                )
                .fetch();
    }

    @Override
    public Optional<EmailAuthCode> findByCode(String email, AuthType authType, String code) {
        EmailAuthCode authCode = queryFactory
                .selectFrom(emailAuthCode)
                .where(
                        this.emailEq(email),
                        this.authTypeEq(authType),
                        this.codeEq(code)
                )
                .fetchFirst();

        return Optional.ofNullable(authCode);
    }

    private BooleanExpression emailEq(String email) {
        return StringUtils.hasText(email) ? emailAuthCode.email.eq(email) : null;
    }

    private BooleanExpression authTypeEq(AuthType authType) {
        return authType != null ? emailAuthCode.authType.eq(authType) : null;
    }

    private BooleanExpression codeEq(String code) {
        return StringUtils.hasText(code) ? emailAuthCode.code.eq(code) : null;
    }

    private BooleanExpression expiredAtGt(LocalDateTime dateTime) {
        return dateTime != null ? emailAuthCode.expiredAt.gt(dateTime) : null;
    }

    private BooleanExpression verifiedAtIsNull() {
        return emailAuthCode.verifiedAt.isNull();
    }
}

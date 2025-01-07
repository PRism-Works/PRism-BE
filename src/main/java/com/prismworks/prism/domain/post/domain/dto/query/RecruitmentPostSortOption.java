package com.prismworks.prism.domain.post.domain.dto.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prismworks.prism.common.dto.page.OrderDirection;
import com.prismworks.prism.common.dto.page.SortOption;
import com.prismworks.prism.domain.post.domain.model.Post;
import com.prismworks.prism.domain.post.domain.model.PostTeamRecruitment;
import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RecruitmentPostSortOption implements SortOption {
    RECENT("recent", OrderDirection.DESC, "createdAt", Post.class),
    POPULAR("popular", OrderDirection.DESC, "viewCount", Post.class),
    EXPIRY("expiry", OrderDirection.DESC, "recruitmentEndAt", PostTeamRecruitment.class);

    private final String value;
    private final OrderDirection direction;
    private final String property;
    private final Class<?> domainClass;

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    public OrderDirection getDirection() {
        return this.direction;
    }

    @Override
    public String getProperty() {
        return this.property;
    }

    @Override
    public Class<?> getDomainClass() {
        return this.domainClass;
    }

    @JsonCreator
    public static RecruitmentPostSortOption from(String value) {
        return Arrays.stream(RecruitmentPostSortOption.values())
            .filter(sort -> sort.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown recruitmentPost value: " + value));
    }
}

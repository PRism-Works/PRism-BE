package com.prismworks.prism.common.infra.repository;

import com.prismworks.prism.common.dto.page.OrderDirection;
import com.prismworks.prism.common.dto.page.SortOption;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryOrderFactory {

    public static OrderSpecifier<?> getOrderSpecifier(SortOption sortOption) {
        return createOrderSpecifier(sortOption);
    }

    public static OrderSpecifier<?>[] getOrderSpecifiers(List<SortOption> sortOptions) {
        return sortOptions.stream()
            .map(QueryOrderFactory::createOrderSpecifier)
            .toArray(OrderSpecifier[]::new);
    }

    private static OrderSpecifier<?> createOrderSpecifier(SortOption sort) {
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<?> entityPath = resolver.createPath(sort.getDomainClass());
        PathBuilder<?> pathBuilder = new PathBuilder<>(entityPath.getType(),
            entityPath.getMetadata());

        Path<?> propertyPath = pathBuilder.get(sort.getProperty());
        Order direction =
            sort.getDirection() == OrderDirection.ASC ? Order.ASC : Order.DESC;
        return new OrderSpecifier(direction, propertyPath);
    }
}

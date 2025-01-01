package com.prismworks.prism.common.dto.page;

public interface SortOption {
    OrderDirection getDirection();
    String getProperty();
    Class<?> getDomainClass();
}

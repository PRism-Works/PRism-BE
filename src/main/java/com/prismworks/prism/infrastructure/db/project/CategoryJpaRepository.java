package com.prismworks.prism.infrastructure.db.project;

import com.prismworks.prism.domain.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}

package com.prismworks.prism.domain.project.Repository;

import com.prismworks.prism.domain.project.Repository.custom.ProjectCustomRepository;
import com.prismworks.prism.domain.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer>, ProjectCustomRepository {
    @Query("SELECT p FROM Project p WHERE p.projectName = :projectName AND p.visibility = true")
    List<Project> findByName(String projectName);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.members m LEFT JOIN p.categories c WHERE " +
            "(:projectName IS NULL OR p.projectName = :projectName) OR " +
            "(:memberName IS NULL OR m.name = :memberName) OR " +
            "(:organizationName IS NULL OR p.organizationName = :organizationName) OR " +
            "(COALESCE(:categories, NULL) IS NULL OR c.category.name IN :categories)")
    List<Project> findByFilters(String projectName, String memberName, List<String> categories, String organizationName);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.email = :email")
    List<Project> findByMemberEmail(String email);

    @Query("SELECT p FROM Project p WHERE p.createdBy = :email")
    List<Project> findByOwnerEmail(String email);
}
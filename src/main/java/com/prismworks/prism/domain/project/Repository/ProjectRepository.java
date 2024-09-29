package com.prismworks.prism.domain.project.Repository;

import com.prismworks.prism.domain.project.Repository.custom.ProjectCustomRepository;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer>, ProjectCustomRepository {
    @Query("SELECT p FROM Project p WHERE p.projectName like %:projectName% AND p.urlVisibility = true")
    List<Project> findByProjectName(String projectName);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.members m LEFT JOIN p.categories c WHERE " +
            "(:projectName IS NULL OR p.projectName = :projectName) OR " +
            "(:memberName IS NULL OR m.name = :memberName) OR " +
            "(:organizationName IS NULL OR p.organizationName = :organizationName) OR " +
            "(COALESCE(:categories, NULL) IS NULL OR c.category.name IN :categories)")
    List<Project> findByFilters(String projectName, String memberName, List<String> categories, String organizationName);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.email = :email")
    List<Project> findByMemberEmail(String email);

    @Query("SELECT DISTINCT p FROM Project p " +
        "LEFT JOIN p.categories c " +
        "LEFT JOIN c.category " +
        "LEFT JOIN p.members m " +
        "WHERE m.user.userId = :userId")
    Page<Project> findByMemberUserId(String userId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.createdBy = :email")
    List<Project> findByOwnerEmail(String email);

    @Query("SELECT DISTINCT p FROM Project p " +
        "LEFT JOIN p.categories pcj " +
        "LEFT JOIN pcj.category c " +
        "LEFT JOIN p.members puj " +
        "LEFT JOIN puj.user u " +
        "WHERE p.createdBy = :email")
    Page<Project> findProjectsWithCategoriesAndMembersByRegister(String email, Pageable pageable);

    Optional<Project> findByProjectIdAndCreatedBy(Integer projectId, String createdBy);

    @Query("SELECT p.anonyVisibility FROM ProjectUserJoin p WHERE p.email = :myEmail AND p.project.projectId = :projectId")
    boolean findByAnonyVisibility(Integer projectId,String myEmail);

    @Query("SELECT p FROM ProjectUserJoin p WHERE p.email = :myEmail AND p.project.projectId = :projectId")
    ProjectUserJoin findByMyEmailAndProjectId(Integer projectId,String myEmail);

    @Query("SELECT DISTINCT p FROM Project p " +
        "LEFT JOIN p.categories c " +
        "LEFT JOIN c.category " +
        "LEFT JOIN p.members m " +
        "WHERE m.email = :email")
    Page<Project> findProjectsWithCategoriesAndMembersByEmail(String email, Pageable pageable);

}

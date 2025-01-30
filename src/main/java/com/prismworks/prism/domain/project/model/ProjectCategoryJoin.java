package com.prismworks.prism.domain.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prismworks.prism.domain.project.dto.command.ProjectCategoryCommonCommand;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "project_category_joins")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectCategoryJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public ProjectCategoryJoin(ProjectCategoryCommonCommand command) {
        this.category = command.getCategory();
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProjectCategoryJoin that = (ProjectCategoryJoin)o;
        return Objects.equals(project.getProjectId(), that.project.getProjectId()) &&
			Objects.equals(category.getCategoryId(), that.category.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(project.getProjectId(), category.getCategoryId());
    }
}

package com.prismworks.prism.domain.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(nullable = false, length = 100)
    private String projectName;

    @Column(length = 1000)
    private String projectDescription;

    @Column(length = 300)
    private String organizationName;

    private int memberCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_hash_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "hash_tag")
    private List<String> hashTags;
    /*
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_categories", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "category")
    private List<String> categories;
    */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ProjectCategoryJoin> categories;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_skills", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "skill")
    private List<String> skills;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", fetch = FetchType.EAGER)
    private List<ProjectUserJoin> members;

    @Column(nullable = false)
    private Boolean visibility;

    @Column(length = 255)
    private String projectUrlLink;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId != null ? projectId.equals(project.projectId) : project.projectId == null;
    }
    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }
}

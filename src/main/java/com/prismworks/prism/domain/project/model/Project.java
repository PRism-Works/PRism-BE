package com.prismworks.prism.domain.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "project_categories", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "category")
    private List<String> categories;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<ProjectUserJoin> members;

    @Column(nullable = false)
    private Boolean visibility;

    @Column(length = 255)
    private String projectUrlLink;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;
}

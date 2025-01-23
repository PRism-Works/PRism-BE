package com.prismworks.prism.domain.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prismworks.prism.common.converter.StringToListConverter;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.*;

import org.hibernate.annotations.BatchSize;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @JsonManagedReference
    private Set<ProjectCategoryJoin> categories = new HashSet<>();

    @Convert(converter = StringToListConverter.class)
    @Column(name = "skills")
    private List<String> skills;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private List<ProjectUserJoin> members = new ArrayList<>();
    /*
    @Column(nullable = true)
    private Boolean visibility;
    */
    // url 링크 공개할지 말지 정하는 옵션값
    @Column
    private Boolean urlVisibility;

    @Column
    private String projectUrlLink;

    @Column(length = 100)
    private String createdBy;

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

    public void updateProject(UpdateProjectCommand command) {

        if (!this.createdBy.equals(command.getCreatedBy())) {
            throw new ProjectException("You do not have permission to update this project", ProjectErrorCode.UNAUTHORIZED);
        }

        String projectName = command.getProjectName();
        if(StringUtils.hasText(projectName) && !projectName.equals(this.projectName)) {
            this.projectName = projectName;
        }

        String projectDescription = command.getProjectDescription();
        if(StringUtils.hasText(projectDescription) && !projectDescription.equals(this.projectDescription)) {
            this.projectDescription = projectDescription;
        }

        String organizationName = command.getOrganizationName();
        if(StringUtils.hasText(organizationName) && !organizationName.equals(this.organizationName)) {
            this.organizationName = organizationName;
        }

        int memberCount = command.getMemberCount();
        if(this.memberCount != memberCount) {
            this.memberCount = memberCount;
        }

        if (!new HashSet<>(this.skills).equals(new HashSet<>(command.getSkills()))) {
            this.skills = command.getSkills();
        }

        Date startDate = command.getStartDate();
        if (startDate != null && !this.startDate.equals(startDate)) {
            this.startDate = startDate;
        }

        Date endDate = command.getEndDate();
        if (endDate != null && !this.endDate.equals(endDate)) {
            this.endDate = endDate;
        }

        this.memberCount = this.getMembers().size();

        this.updatedAt = new Date();
    }
}

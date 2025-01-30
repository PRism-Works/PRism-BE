package com.prismworks.prism.domain.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prismworks.prism.common.converter.StringToListConverter;
import com.prismworks.prism.domain.project.dto.command.CreateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectCategoryCommonCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectMemberCommonCommand;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;

import jakarta.persistence.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

import java.util.*;

import org.hibernate.annotations.BatchSize;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public Project(CreateProjectCommand command) {
        this.projectName = command.getProjectName();
        this.projectDescription = command.getProjectDescription();
        this.organizationName = command.getOrganizationName();
        this.skills = command.getSkills();
        this.startDate = command.getStartDate();
        this.endDate = command.getEndDate();
        this.projectUrlLink = command.getProjectUrlLink();
        this.createdBy = command.getCreatedBy();
        this.urlVisibility = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();

        command.getMembers()
            .stream()
            .map(ProjectUserJoin::new)
            .forEach(this::addMember);

        command.getCategories()
            .stream()
            .map(ProjectCategoryJoin::new)
            .forEach(this::addCategory);
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

        updateMembers(command.getMembers());

        updateCategory(command.getCategories());

        this.updatedAt = new Date();
    }

    public void updateMembers(List<ProjectMemberCommonCommand> commands) {
        Map<String, ProjectMemberCommonCommand> map = commands.stream()
            .collect(Collectors.toMap(
                ProjectMemberCommonCommand::getEmail,
                command -> command
            ));

        List<ProjectUserJoin> membersCopy = new ArrayList<>(this.members);
        for(ProjectUserJoin member : membersCopy) {
            ProjectMemberCommonCommand command = map.get(member.getEmail());

            if(command != null) {
                member.update(command);
                map.remove(member.getEmail());
            } else {
                this.removeMember(member);
            }
        }

        for(ProjectMemberCommonCommand command : map.values()) {
            ProjectUserJoin member = new ProjectUserJoin(command);
            this.addMember(member);
        }
    }

    public void addMember(ProjectUserJoin member) {
        members.add(member);
        member.setProject(this);

        this.updateMemberCount();
    }

    public void removeMember(ProjectUserJoin member) {
        members.remove(member);
        member.setProject(null);

        this.updateMemberCount();
    }

    public void updateMemberCount() {
        this.memberCount = members.size();
    }

    private void updateCategory(List<ProjectCategoryCommonCommand> commands) {

        Set<Category> newCategories = commands.stream()
            .map(ProjectCategoryCommonCommand::getCategory)
            .collect(Collectors.toSet());

        new HashSet<>(this.categories).stream()
            .filter(categoryJoin -> !newCategories.contains(categoryJoin.getCategory()))
            .forEach(this::removeCategory);

        for (ProjectCategoryCommonCommand command : commands) {
            addCategory(new ProjectCategoryJoin(command));
        }
    }

    private void addCategory(ProjectCategoryJoin category) {
        category.setProject(this);
        categories.add(category);
    }

    private void removeCategory(ProjectCategoryJoin category) {
        categories.remove(category);
        category.setProject(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectId, project.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }
}

package com.prismworks.prism.domain.project.model;

import com.prismworks.prism.domain.user.model.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "project_user_joins")
public class ProjectUserJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectUserJoinsId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String name;
    private String email;

    @ElementCollection
    @CollectionTable(name = "project_user_skills", joinColumns = @JoinColumn(name = "project_user_joins_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ElementCollection
    @CollectionTable(name = "project_user_roles", joinColumns = @JoinColumn(name = "project_user_joins_id"))
    @Column(name = "role")
    private List<String> roles;
}

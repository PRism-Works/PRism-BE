package com.prismworks.prism.domain.project.Repository;

import com.prismworks.prism.domain.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {;}

package com.prismworks.prism.domain.project.model;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project_skills")
public class ProjectSkills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "project_id")
	private Integer projectId;

	@Column(name = "skill", nullable = false)
	private String skill;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProjectSkills that = (ProjectSkills) o;
		return Objects.equals(skill, that.skill);
	}

	@Override
	public int hashCode() {
		return Objects.hash(skill);
	}
}

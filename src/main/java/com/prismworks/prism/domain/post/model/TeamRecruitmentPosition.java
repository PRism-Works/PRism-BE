package com.prismworks.prism.domain.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "team_recruitment_position")
@Entity
public class TeamRecruitmentPosition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_recruitment_position_id")
	private Long positionId;

	@Column(name = "post_team_recruitment_id")
	private Long postTeamRecruitmentId;

	@Enumerated(EnumType.STRING)
	@Column(name = "recruitment_position")
	private RecruitmentPosition position;

	@Column(name = "recruitment_count")
	private Integer recruitmentCount;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}

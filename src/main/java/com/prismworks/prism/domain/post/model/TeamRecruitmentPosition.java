package com.prismworks.prism.domain.post.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.prismworks.prism.domain.post.dto.command.TeamRecruitmentPositionCommand.CreateTeamRecruitmentPosition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_team_recruitment_id")
	private PostTeamRecruitment postTeamRecruitment;

	@Enumerated(EnumType.STRING)
	@Column(name = "recruitment_position")
	private RecruitmentPosition position;

	@Column(name = "recruitment_count")
	private int recruitmentCount;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public TeamRecruitmentPosition(CreateTeamRecruitmentPosition command) {
		this.position = command.getPosition();
		this.recruitmentCount = command.getRecruitmentCount();
		this.createdAt = LocalDateTime.now();
	}

	public void setPostTeamRecruitment(PostTeamRecruitment postTeamRecruitment) {
		this.postTeamRecruitment = postTeamRecruitment;
	}
}

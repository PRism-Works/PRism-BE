package com.prismworks.prism.domain.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "post_team_recruitment")
@Entity
public class PostTeamRecruitment {

	@Id
	@Column(name = "post_team_recruitment_id")
	private Integer postTeamRecruitmentId;

	@Column(name = "post_id")
	private Long postId;

	@Column(name = "project_id")
	private Integer projectId;

	@Column(name = "contact_method")
	private String contactMethod;

	@Column(name = "contact_info")
	private String contactInfo;

	@Column(name = "apply_method")
	private String applyMethod;

	@Column(name = "apply_info")
	private String applyInfo;

	@Column(name = "process_method")
	private String processMethod;

	@Column(name = "recruitment_start_at")
	private LocalDateTime recruitmentStartAt;

	@Column(name = "recruitment_end_at")
	private LocalDateTime recruitmentEndAt;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}

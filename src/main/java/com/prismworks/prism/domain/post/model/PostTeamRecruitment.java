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
@Table(name = "post_team_recruitment")
@Entity
public class PostTeamRecruitment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_team_recruitment_id")
	private Long postTeamRecruitmentId;

	@Column(name = "post_id")
	private Long postId;

	@Column(name = "project_id")
	private Integer projectId;

	@Enumerated(EnumType.STRING)
	@Column(name = "contact_method")
	private ContactMethod contactMethod;

	@Enumerated(EnumType.STRING)
	@Column(name = "recruitment_status")
	private RecruitmentStatus recruitmentStatus;

	@Column(name = "contact_info")
	private String contactInfo;

	@Enumerated(EnumType.STRING)
	@Column(name = "apply_method")
	private ApplyMethod applyMethod;

	@Column(name = "apply_info")
	private String applyInfo;

	@Enumerated(EnumType.STRING)
	@Column(name = "process_method")
	private ProcessMethod processMethod;

	@Column(name = "is_open_until_recruited")
	private boolean isOpenUntilRecruited;

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

package com.prismworks.prism.domain.post.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "post_team_recruitment")
public class PostTeamRecruitment {

	@Id
	@Column(name = "post_id")
	private Integer postId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "project_id")
	private Integer projectId;

	@Column(name = "recruitment_start", nullable = false)
	private LocalDateTime recruitmentStart;

	@Column(name = "recruitment_end")
	private LocalDateTime recruitmentEnd;

	@Column(name = "contact_method", nullable = false)
	private Integer contactMethod;

	@Column(name = "contact_info", length = 255)
	private String contactInfo;

	@Column(name = "application_method", nullable = false, length = 255)
	private Integer applicationMethod;

	@Column(name = "application_info", length = 255)
	private String applicationInfo;

	@Column(name = "process_method", length = 255)
	private String processMethod;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "postTeamRecruitment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();
}

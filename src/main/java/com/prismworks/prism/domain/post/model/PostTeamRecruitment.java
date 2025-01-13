package com.prismworks.prism.domain.post.model;

import com.prismworks.prism.domain.post.dto.command.CreateRecruitmentPostCommand;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@NoArgsConstructor
@Table(name = "post_team_recruitment")
@Entity
public class PostTeamRecruitment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_team_recruitment_id")
	private Long postTeamRecruitmentId;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "project_id")
	private Integer projectId;

	@Enumerated(EnumType.STRING)
	@Column(name = "recruitment_status")
	private RecruitmentStatus recruitmentStatus = RecruitmentStatus.RECRUITING;

	@Enumerated(EnumType.STRING)
	@Column(name = "contact_method")
	private ContactMethod contactMethod;

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

	@OneToMany(mappedBy = "postTeamRecruitment", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@BatchSize(size = 100)
	private Set<TeamRecruitmentPosition> recruitmentPositions = new HashSet<>();

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

	public PostTeamRecruitment(CreateRecruitmentPostCommand command) {
		this.projectId = command.getProjectId();
		this.contactMethod = command.getContactMethod();
		this.contactInfo = command.getContactInfo();
		this.applyMethod = command.getApplyMethod();
		this.applyInfo = command.getApplyInfo();
		this.processMethod = command.getProcessMethod();
		this.isOpenUntilRecruited = command.isOpenUntilRecruited();
		this.recruitmentStartAt = command.getRecruitmentStartAt();
		this.recruitmentEndAt = command.getRecruitmentEndAt();
		this.createdAt = LocalDateTime.now();
		this.post = new Post(command.getUserId(), command.getTitle(), command.getContent());

		command.getRecruitPositions()
			.stream()
			.map(position -> new TeamRecruitmentPosition(position.getPosition(), position.getCount()))
			.forEach(this::addRecruitmentPosition);
	}

	public void addRecruitmentPosition(TeamRecruitmentPosition position) {
		if(!this.recruitmentPositions.contains(position)) {
			this.recruitmentPositions.add(position);
			position.setPostTeamRecruitment(this);
		}
	}
}

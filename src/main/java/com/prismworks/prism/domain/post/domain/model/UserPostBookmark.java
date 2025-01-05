package com.prismworks.prism.domain.post.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_post_bookmark")
@Getter
@Setter
@NoArgsConstructor
public class UserPostBookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "active_flag")
	private boolean activeFlag = true;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
}

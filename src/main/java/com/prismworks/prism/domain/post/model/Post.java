package com.prismworks.prism.domain.post.model;

import com.prismworks.prism.domain.post.dto.command.PostCommand.CreatePost;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "post")
@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "view_count")
	private Integer viewCount = 0;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public Post(CreatePost command) {
		this.userId = command.getUserId();
		this.title = command.getTitle();
		this.content = command.getContent();
		this.createdAt = LocalDateTime.now();
	}
}

package com.prismworks.prism.domain.peerreview.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = PeerReviewLinkCode.TABLE_NAME)
@Entity
public class PeerReviewLinkCode {

    public static final String TABLE_NAME = "peer_review_link_code";

    @Column(name = "peer_review_link_code_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "link_code")
    private String code;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "reviewer_email")
    private String reviewerEmail;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

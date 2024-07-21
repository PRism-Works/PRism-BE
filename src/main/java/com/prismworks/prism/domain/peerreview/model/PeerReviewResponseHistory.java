package com.prismworks.prism.domain.peerreview.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = PeerReviewResponseHistory.TABLE_NAME)
@Entity
public class PeerReviewResponseHistory {

    public static final String TABLE_NAME = "peer_review_response_history";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peer_review_response_history_id")
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "reviewer_id")
    private String reviewerId;

    @Column(name = "reviewee_id")
    private String revieweeId;

    @Column(name = "reviewer_email")
    private String reviewerEmail;

    @Column(name = "reviewee_email")
    private String revieweeEmail;

    @Column(name = "review_response")
    private String reviewResponse;
}

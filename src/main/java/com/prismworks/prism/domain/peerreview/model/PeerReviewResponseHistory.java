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

    @Column(name = "communication_score")
    public Float communicationScore;

    @Column(name = "initiative_score")
    public Float initiativeScore;

    @Column(name = "problem_solving_ability_score")
    public Float problemSolvingAbilityScore;

    @Column(name = "responsibility_score")
    public Float responsibilityScore;

    @Column(name = "teamwork_score")
    public Float teamworkScore;

    @Column(name = "strength_feedback")
    public String strengthFeedback;

    @Column(name = "improvement_point_feedback")
    public String improvePointFeedback;

    @Column(name = "review_response")
    private String reviewResponse;
}

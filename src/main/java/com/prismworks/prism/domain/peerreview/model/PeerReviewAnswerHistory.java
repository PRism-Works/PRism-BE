package com.prismworks.prism.domain.peerreview.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = PeerReviewAnswerHistory.TABLE_NAME)
@Entity
public class PeerReviewAnswerHistory {

    public static final String TABLE_NAME = "peer_review_answer_history";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peer_review_answer_id")
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "evaluator_id")
    private String evaluatorId;

    @Column(name = "evaluatee_id")
    private String evaluateeId;

    @Column(name = "evaluator_email")
    private String evaluatorEmail;

    @Column(name = "evaluatee_email")
    private String evaluateeEmail;

    @Column(name = "review_answer")
    private String reviewAnswer;
}

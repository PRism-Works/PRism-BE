package com.prismworks.prism.domain.peerreview.model;

import com.prismworks.prism.common.converter.StringToListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = PeerReviewResult.TABLE_NAME)
@Entity
public class PeerReviewResult {
    public static final String TABLE_NAME = "peer_review_result";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peer_review_result_id")
    @Id
    public Integer id;

    @Column(name = "project_id")
    public Integer projectId;

    @Column(name = "user_id")
    public String userId;

    @Column(name = "email")
    public String email;

    @Convert(converter = StringToListConverter.class)
    @Column(name = "reviewer_email_list")
    List<String> reviewerEmailList;

    @Column(name = "responsibility_score")
    public Float responsibilityScore;

    @Column(name = "initiative_score")
    public Float initiativeScore;

    @Column(name = "problem_solving_ability_score")
    public Float problemSolvingAbilityScore;

    @Column(name = "communication_score")
    public Float communicationScore;

    @Column(name = "cooperation_score")
    public Float cooperationScore;

    @Column(name = "total_feedback")
    public String totalFeedback;

    @Builder.Default
    @Column(name = "created_at")
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    public LocalDateTime updatedAt;

    @Column(name = "prism_type")
    public String prismType;

    public void updateResult(PrismData prismData) {
        this.responsibilityScore = prismData.getResponsibilityScore();
        this.communicationScore = prismData.getCommunicationScore();
        this.cooperationScore = prismData.getCooperationScore();
        this.problemSolvingAbilityScore = prismData.getProblemSolvingAbilityScore();
        this.initiativeScore = prismData.getInitiativeScore();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateResult(float responsibilityScore, float communicationScore, float teamworkScore,
                             float problemSolvingAbilityScore, float initiativeScore)
    {
        this.responsibilityScore = responsibilityScore;
        this.communicationScore = communicationScore;
        this.cooperationScore = teamworkScore;
        this.problemSolvingAbilityScore = problemSolvingAbilityScore;
        this.initiativeScore = initiativeScore;
        this.updatedAt = LocalDateTime.now();
    }
}

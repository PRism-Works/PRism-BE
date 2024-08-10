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
@Table(name = PeerReviewTotalResult.TABLE_NAME)
@Entity
public class PeerReviewTotalResult {

    public static final String TABLE_NAME = "peer_review_total_result";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peer_review_total_result_id")
    @Id
    public Integer id;

    @Column(name = "project_id")
    public Integer projectId;

    @Column(name = "user_id")
    public String userId;

    @Column(name = "email")
    public String email;

    @Column(name = "reliability_score")
    public Float reliabilityScore;

    @Column(name = "teamwork_score")
    public Float teamworkScore;

    @Column(name = "leadership_score")
    public Float leadershipScore;

    @Convert(converter = StringToListConverter.class)
    @Column(name = "keywords")
    List<String> keywords;

    @Column(name = "evalution")
    String evalution;

    @Builder.Default
    @Column(name = "created_at")
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    public LocalDateTime updatedAt;

    @Column(name = "prism_type")
    public String prismType;

    public void updateResult(PrismData prismData) {
        this.reliabilityScore = prismData.getReliabilityScore();
        this.teamworkScore = prismData.getTeamworkScore();
        this.leadershipScore = prismData.getLeadershipScore();
        this.keywords = prismData.getPrismSummaryData().getKeywords();
        this.evalution = prismData.getPrismSummaryData().getReviewSummary();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateResult(float reliabilityScore, float teamworkScore, float leadershipScore, List<String> keywords,
                             String evalution)
    {
        this.reliabilityScore = reliabilityScore;
        this.teamworkScore = teamworkScore;
        this.leadershipScore = leadershipScore;
        this.keywords = keywords;
        this.evalution = evalution;
        this.updatedAt = LocalDateTime.now();
    }
}

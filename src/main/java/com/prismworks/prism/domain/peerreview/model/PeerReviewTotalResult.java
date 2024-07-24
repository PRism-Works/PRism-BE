package com.prismworks.prism.domain.peerreview.model;

import com.prismworks.prism.common.converter.StringToListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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

    @Column(name = "responsibility_score")
    public Float responsibilityScore;

    @Column(name = "teamwork_score")
    public Float teamworkScore;

    @Column(name = "leadership_score")
    public Float leadership_score;

    @Convert(converter = StringToListConverter.class)
    @Column(name = "keywords")
    List<String> keywords;

    @Column(name = "total_feedback")
    public String totalFeedback;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updatedAt")
    public LocalDateTime updatedAt;
}
package com.prismworks.prism.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProjectPeerReviewEmailInfoDto {
    private Integer projectId;
    private String projectName;
    private String ownerName;
    private List<String> notReviewingMemberEmails;
}

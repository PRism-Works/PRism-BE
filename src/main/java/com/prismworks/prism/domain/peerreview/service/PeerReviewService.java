package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.peerreview.converter.PeerReviewResponseConverter;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.exception.PeerReviewException;
import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.project.dto.ProjectPeerReviewEmailInfoDto;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.*;

@RequiredArgsConstructor
@Repository
public class PeerReviewService {

    private final PeerReviewResponseHistoryService peerReviewResponseHistoryService;
    private final PeerReviewResultService peerReviewResultService;
    private final PeerReviewLinkCodeService peerReviewLinkCodeService;
    private final ProjectService projectService; // todo: 분리

    private final PeerReviewResponseConverter peerReviewResponseConverter;

    public List<PeerReviewLinkCode> createPeerReviewLinkCode(Integer projectId, String ownerEmail) {
        ProjectPeerReviewEmailInfoDto projectInfo = projectService.getProjectPeerReviewEmailInfo(projectId, ownerEmail);
        List<String> notReviewingMemberEmails = projectInfo.getNotReviewingMemberEmails();

        return peerReviewLinkCodeService.createLinkCode(projectId, notReviewingMemberEmails);
    }

    public ReviewLinkInfoResponse getReviewLinkInfo(String code) {
        PeerReviewLinkCode linkCode = peerReviewLinkCodeService.getLinkCode(code);
        Integer projectId = linkCode.getProjectId();
        String reviewerEmail = linkCode.getReviewerEmail();

        List<ProjectUserJoin> allMemberInProject = projectService.getAllMemberInProject(projectId);
        if(allMemberInProject.isEmpty()) {
            throw PeerReviewException.REVIEWEE_NOT_EXIST;
        }

        List<String> revieweeEmails = new ArrayList<>();
        for(ProjectUserJoin member : allMemberInProject) {
            String email = member.getEmail();
            if(reviewerEmail.equals(email)) {
                if(member.isPeerReviewDone()) {
                    throw PeerReviewException.ALREADY_FINISH_REVIEW;
                }
            } else {
                revieweeEmails.add(email);
            }
        }

        return PeerReviewDto.ReviewLinkInfoResponse.builder()
                .revieweeEmails(revieweeEmails)
                .projectId(projectId)
                .build();
    }

    public void createPeerReviewResponseHistory(Integer projectId,
                                                CreatePeerReviewResponseRequest request)
    {
        List<PeerReviewResponseHistory> histories = peerReviewResponseConverter.toPeerReviewResponseHistories(projectId, request);
        peerReviewResponseHistoryService.saveAllHistories(histories);
    }
}

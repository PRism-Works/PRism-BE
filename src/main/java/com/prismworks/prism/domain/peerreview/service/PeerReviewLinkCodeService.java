package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.email.util.RandomStringGenerator;
import com.prismworks.prism.domain.peerreview.exception.PeerReviewException;
import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PeerReviewLinkCodeService {

    private final PeerReviewRepository peerReviewRepository;

    @Transactional(readOnly = true)
    public PeerReviewLinkCode getLinkCode(String linkCode) {
        return peerReviewRepository.getPeerReviewLinkCodeByCode(linkCode)
                .orElseThrow(() -> PeerReviewException.LINK_CODE_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewLinkCode> getLinkCodes(Integer projectId, List<String> reviewerEmails) {
        return peerReviewRepository.getPeerReviewLinkCodeByCodesByProjectIdAndReviewerEmailIn(projectId, reviewerEmails);
    }

    @Transactional
    public List<PeerReviewLinkCode> createLinkCode(Integer projectId, List<String> reviewerEmails) {
        List<PeerReviewLinkCode> linkCodes = this.getLinkCodes(projectId, reviewerEmails);
        List<String> alreadyCreatedCodeEmails = linkCodes.stream().map(PeerReviewLinkCode::getReviewerEmail).toList();
        reviewerEmails.removeAll(alreadyCreatedCodeEmails);

        List<PeerReviewLinkCode> peerReviewLinkCodes = new ArrayList<>();
        for(String email : reviewerEmails) {
            String randomCode = RandomStringGenerator.generate(10);
            PeerReviewLinkCode linkCode = PeerReviewLinkCode.builder()
                    .code(randomCode)
                    .projectId(projectId)
                    .reviewerEmail(email)
                    .build();

            peerReviewLinkCodes.add(linkCode);
        }

        List<PeerReviewLinkCode> savedPeerReviewLinkCodes = peerReviewRepository.saveAllPeerReviewLinkCodes(peerReviewLinkCodes);

        linkCodes.addAll(savedPeerReviewLinkCodes);
        return linkCodes;
    }
}

package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.email.util.RandomStringGenerator;
import com.prismworks.prism.domain.peerreview.exception.PeerReviewException;
import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewLinkCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PeerReviewLinkCodeService {

    private final PeerReviewLinkCodeRepository peerReviewLinkCodeRepository;

    @Transactional(readOnly = true)
    public PeerReviewLinkCode getLinkCode(String linkCode) {
        return peerReviewLinkCodeRepository.findByCode(linkCode)
                .orElseThrow(() -> PeerReviewException.LINK_CODE_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewLinkCode> getLinkCodes(Integer projectId, List<String> reviewerEmails) {
        return peerReviewLinkCodeRepository.findByProjectIdAndReviewerEmailIn(projectId, reviewerEmails);
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

        return peerReviewLinkCodeRepository.saveAll(peerReviewLinkCodes);
    }
}

package com.prismworks.prism.domain.peerreview.repository;

import java.util.List;
import java.util.Optional;

import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;

public interface PeerReviewRepository {
	PeerReviewResult savePeerReviewResult(PeerReviewResult peerReviewResult);
	Optional<PeerReviewResult> getPeerReviewResultByUserIdAndPrismType(String userId, String prismType);
	Optional<PeerReviewResult> getPeerReviewResultByProjectIdAndEmail(Integer projectId, String email);
	Optional<PeerReviewResult> getPeerReviewResultByEmailAndPrismType(String email, String prismType);
	Optional<PeerReviewResult> getPeerReviewResuiltByUserIdAndProjectIdAndPrismType
		(
			String userId,
			int projectId,
			String prismType
		);
	List<PeerReviewResult> getPeerReviewResultsByEmailAndPrismType(String email, String prismType);

	PeerReviewTotalResult savePeerReviewTotalResult(PeerReviewTotalResult peerReviewTotalResult);
	PeerReviewTotalResult getPeerReviewTotalResultByProjectIdAndUserIdAndPrismType(int projectId, String userId, String prismType);
	PeerReviewTotalResult getPeerReviewTotalResultByProjectIdAndEmailAndPrismType(int projectId, String email, String prismType);
	Optional<PeerReviewTotalResult> getPeerReviewTotalResultByUserIdAndProjectIdAndPrismType(String userId, int projectId,String prismType);
	Optional<PeerReviewTotalResult> getPeerReviewTotalResultByEmailAndPrismType(String email, String prismType);
	Optional<PeerReviewTotalResult> getPeerReviewTotalResultByUserIdAndPrismType(String userId,String prismType);
	Optional<PeerReviewTotalResult> getPeerReviewTotalResultByProjectIdAndEmail(Integer projectId, String email);
	List<PeerReviewTotalResult> getPeerReviewTotalResultsByEmailAndPrismType(String email, String prismType);

	Optional<PeerReviewLinkCode> getPeerReviewLinkCodeByCode(String code);
	List<PeerReviewLinkCode> getPeerReviewLinkCodeByCodesByProjectIdAndReviewerEmailIn(int projectId, List<String> reviewerEmails);
	List<PeerReviewLinkCode> saveAllPeerReviewLinkCodes(List<PeerReviewLinkCode> peerReviewLinkCodes);
}

package com.prismworks.prism.infrastructure.db.peerreview.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewRepository;
import com.prismworks.prism.infrastructure.db.peerreview.PeerReviewLinkCodeJpaRepository;
import com.prismworks.prism.infrastructure.db.peerreview.PeerReviewResponseHistoryJpaRepository;
import com.prismworks.prism.infrastructure.db.peerreview.PeerReviewResultJpaRepository;
import com.prismworks.prism.infrastructure.db.peerreview.PeerReviewTotalResultJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PeerReviewRepositoryImpl implements PeerReviewRepository {

	private final PeerReviewLinkCodeJpaRepository peerReviewLinkCodeJpaRepository;
	private final PeerReviewResponseHistoryJpaRepository peerReviewResponseHistoryJpaRepository;
	private final PeerReviewResultJpaRepository peerReviewResultJpaRepository;
	private final PeerReviewTotalResultJpaRepository peerReviewTotalResultJpaRepository;

	@Override
	public PeerReviewResult savePeerReviewResult(PeerReviewResult peerReviewResult) {
		return peerReviewResultJpaRepository.save(peerReviewResult);
	}

	@Override
	public Optional<PeerReviewResult> getPeerReviewResultByUserIdAndPrismType(String userId,String prismType) {
		return peerReviewResultJpaRepository.findByUserIdAndPrismType(userId, prismType);
	}

	@Override
	public Optional<PeerReviewResult> getPeerReviewResultByProjectIdAndEmail(Integer projectId, String email) {
		return peerReviewResultJpaRepository.findByProjectIdAndEmail(projectId, email);
	}

	@Override
	public Optional<PeerReviewResult> getPeerReviewResultByEmailAndPrismType(String email, String prismType) {
		return peerReviewResultJpaRepository.findByEmailAndPrismType(email, prismType);
	}

	@Override
	public Optional<PeerReviewResult> getPeerReviewResuiltByUserIdAndProjectIdAndPrismType
		(
			String userId,
			int projectId,
			String prismType
		)
	{
		return peerReviewResultJpaRepository.findByUserIdAndProjectIdAndPrismType(userId, projectId,prismType);
	}

	@Override
	public List<PeerReviewResult> getPeerReviewResultsByEmailAndPrismType(String email, String prismType) {
		return peerReviewResultJpaRepository.findAllByEmailAndPrismType(email, prismType);
	}

	@Override
	public PeerReviewTotalResult savePeerReviewTotalResult(PeerReviewTotalResult peerReviewTotalResult) {
		return peerReviewTotalResultJpaRepository.save(peerReviewTotalResult);
	}

	@Override
	public PeerReviewTotalResult getPeerReviewTotalResultByProjectIdAndUserIdAndPrismType(int projectId, String userId, String prismType) {
		return peerReviewTotalResultJpaRepository.findByProjectIdAndUserIdAndPrismType(projectId, userId, prismType);
	}

	@Override
	public PeerReviewTotalResult getPeerReviewTotalResultByProjectIdAndEmailAndPrismType(int projectId, String email, String prismType) {
		return peerReviewTotalResultJpaRepository.findByProjectIdAndEmailAndPrismType(projectId, email, prismType);
	}

	@Override
	public Optional<PeerReviewTotalResult> getPeerReviewTotalResultByUserIdAndProjectIdAndPrismType(String userId, int projectId,String prismType) {
		return peerReviewTotalResultJpaRepository.findByUserIdAndProjectIdAndPrismType(userId, projectId, prismType);
	}

	@Override
	public Optional<PeerReviewTotalResult> getPeerReviewTotalResultByEmailAndPrismType(String email, String prismType) {
		return peerReviewTotalResultJpaRepository.findByEmailAndPrismType(email, prismType);
	}

	@Override
	public Optional<PeerReviewTotalResult> getPeerReviewTotalResultByUserIdAndPrismType(String userId,String prismType) {
		return peerReviewTotalResultJpaRepository.findByUserIdAndPrismType(userId,prismType);
	}

	@Override
	public Optional<PeerReviewTotalResult> getPeerReviewTotalResultByProjectIdAndEmail(Integer projectId, String email) {
		return peerReviewTotalResultJpaRepository.findByProjectIdAndEmail(projectId, email);
	}

	@Override
	public List<PeerReviewTotalResult> getPeerReviewTotalResultsByEmailAndPrismType(String email, String prismType) {
		return peerReviewTotalResultJpaRepository.findAllByEmailAndPrismType(email, prismType);
	}

	@Override
	public Optional<PeerReviewLinkCode> getPeerReviewLinkCodeByCode(String code) {
		return peerReviewLinkCodeJpaRepository.findByCode(code);
	}

	@Override
	public List<PeerReviewLinkCode> getPeerReviewLinkCodeByCodesByProjectIdAndReviewerEmailIn
		(
			int projectId,
			List<String> reviewerEmails
		) {
		return peerReviewLinkCodeJpaRepository.findByProjectIdAndReviewerEmailIn(projectId, reviewerEmails);
	}

	@Override
	public List<PeerReviewLinkCode> saveAllPeerReviewLinkCodes(List<PeerReviewLinkCode> peerReviewLinkCodes) {
		return peerReviewLinkCodeJpaRepository.saveAll(peerReviewLinkCodes);
	}
}

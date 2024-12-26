package com.prismworks.prism.domain.post.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.search.dto.SearchDto;

public interface PostTeamRecruitmentRepositoryCustom {

	Page<SearchDto.PostTeamRecruitmentSearchResponse> findBookmarkedPostTeamRecruitments(
		@Param("userId") String userId,
		@Param("positions") Set<String> positions,
		@Param("categories") List<String> categories,
		@Param("skills") List<String> skills,
		@Param("processMethod") ProcessMethod processMethod,
		@Param("recruitmentStatus") RecruitmentStatus recruitmentStatus,
		Pageable pageable
	);
}

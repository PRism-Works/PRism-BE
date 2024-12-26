package com.prismworks.prism.domain.search.service;

import com.prismworks.prism.common.dto.PageResponse;
import com.prismworks.prism.domain.post.repository.PostTeamRecruitmentRepository;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.Repository.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.search.dto.ProjectSearchCondition;
import com.prismworks.prism.domain.search.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ProjectRepository projectRepository;
	private final PostTeamRecruitmentRepository postTeamRecruitmentRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProjectProjection.ProjectSearchResult> searchProject(SearchDto.ProjectSearchRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNo(), dto.getPageSize());
        ProjectSearchCondition condition = dto.getCondition();

        Page<ProjectProjection.ProjectSearchResult> page = projectRepository.searchByCondition(condition, pageRequest);

        return new PageResponse<>(page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getContent());
    }

	public Page<SearchDto.PostTeamRecruitmentSearchResponse> getBookmarkedPostTeamRecruitments(
		String userId,
		SearchDto.PostTeamRecruitmentSearchRequest request,
		PageRequest pageRequest
	)
	{
		return postTeamRecruitmentRepository.findBookmarkedPostTeamRecruitments(
			userId,
			request.getPositions(),
			request.getCategories(),
			request.getSkills(),
			request.getProcessMethod(),
			request.getRecruitmentStatus(),
			pageRequest
		);
	}
}

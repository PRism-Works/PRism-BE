package com.prismworks.prism.infrastructure.db.project.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.infrastructure.db.project.custom.ProjectCustomRepository;
import com.prismworks.prism.infrastructure.db.project.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.project.model.Category;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.infrastructure.db.project.CategoryJpaRepository;
import com.prismworks.prism.infrastructure.db.project.ProjectJpaRepository;
import com.prismworks.prism.infrastructure.db.project.ProjectUserJoinJpaRepository;
import com.prismworks.prism.interfaces.search.dto.ProjectSearchCondition;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ProjectRepositoryImpl implements ProjectRepository {
	private final ProjectCustomRepository projectCustomRepository;
	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectUserJoinJpaRepository projectUserJoinJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;


	@Override
	public void saveProject(Project project) {
		projectJpaRepository.save(project);
	}

	@Override
	public void deleteProject(Project project) {
		projectJpaRepository.delete(project);
	}

	@Override
	public Optional<Project> getProjectById(int projectId) {
		return projectJpaRepository.findById(projectId);
	}

	@Override
	public Optional<Project> getProjectByIdAndCreator(int projectId, String email) {
		return projectJpaRepository.findByProjectIdAndCreatedBy(projectId, email);
	}

	@Override
	public List<Project> getProjectsByName(String projectName) {
		return projectJpaRepository.findByProjectName(projectName);
	}

	@Override
	public List<Project> getProjectsByFilters(
		String projectName,
		String memberName,
		List<String> categories,
		String organizationName
	) {
		return projectJpaRepository.findByFilters(projectName, memberName, categories, organizationName);
	}

	@Override
	public List<Project> getProjectsByMemberId(String memberUserId) {
		return projectJpaRepository.findByMemberUserId(memberUserId);
	}

	@Override
	public List<Project> getProjectsByMemberEmail(String email) {
		return projectJpaRepository.findByMemberEmail(email);
	}

	@Override
	public List<Project> getProjectsByOwnerEmail(String email) {
		return projectJpaRepository.findByOwnerEmail(email);
	}

	@Override
	public List<ProjectUserJoin> getMembersByProjectId(int projectId) {
		return projectCustomRepository.findAllMemberByProjectId(projectId);
	}

	@Override
	public Page<ProjectProjection.ProjectSearchResult> searchByCondition(ProjectSearchCondition condition, PageRequest pageRequest) {
		return projectCustomRepository.searchByCondition(condition, pageRequest);
	}

	@Override
	public Optional<Category> getCategoryByName(String categoryName) {
		return categoryJpaRepository.findByName(categoryName);
	}

	@Override
	public Long getMemberCountByProjectId(int projectId) {
		return projectCustomRepository.countUserByProjectId(projectId);
	}

	@Override
	public ProjectUserJoin saveProjectMember(ProjectUserJoin projectMember) {
		return projectUserJoinJpaRepository.save(projectMember);
	}

	@Override
	public int getProjectCountByUserId(String userId) {
		return projectUserJoinJpaRepository.countByUserId(userId);
	}

	@Override
	public ProjectUserJoin getMemberByEmailAndProjectId(int projectId, String reviewerEmail) {
		return  projectUserJoinJpaRepository.findByEmailAndProjectId(reviewerEmail, projectId);
	}

	@Override
	public int countSurveyParticipantsByProjectId(int projectId) {
		return projectUserJoinJpaRepository.getSurveyParticipant(projectId);
	}

	@Override
	public boolean getAnonyVisibilityByProjectIdAndEmail(int projectId, String email) {
		return projectJpaRepository.findByAnonyVisibility(projectId, email);
	}

}

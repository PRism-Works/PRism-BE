package com.prismworks.prism.domain.project.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.prismworks.prism.infrastructure.db.project.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.project.model.Category;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.interfaces.search.dto.ProjectSearchCondition;

public interface ProjectRepository {
	void saveProject(Project project);
	void deleteProject(Project project);
	Optional<Project> getProjectById(int projectId);
	Optional<Project> getProjectByIdAndCreator(int projectId, String email);
	List<Project> getProjectsByName(String projectName);
	List<Project> getProjectsByFilters(String projectName, String memberName, List<String> categories, String organizationName);
	List<Project> getProjectsByMemberId(String memberUserId);
	List<Project> getProjectsByMemberEmail(String email);
	List<Project> getProjectsByOwnerEmail(String email);
	List<ProjectUserJoin> getMembersByProjectId(int projectId);
	Page<ProjectProjection.ProjectSearchResult> searchByCondition(ProjectSearchCondition condition, PageRequest pageRequest);
	Optional<Category> getCategoryByName(String categoryName);
	Long getMemberCountByProjectId(int projectId);

	ProjectUserJoin saveProjectMember(ProjectUserJoin projectMember);
	int getProjectCountByUserId(String userId);
	ProjectUserJoin getMemberByEmailAndProjectId(int projectId, String reviewerEmail);
	int countSurveyParticipantsByProjectId(int projectId);
	boolean getAnonyVisibilityByProjectIdAndEmail(int projectId, String email);


}

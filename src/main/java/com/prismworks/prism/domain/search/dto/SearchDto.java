package com.prismworks.prism.domain.search.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.prismworks.prism.domain.search.dto.ProjectSearchCondition.SearchType;

import com.prismworks.prism.domain.post.domain.model.ProcessMethod;
import com.prismworks.prism.domain.post.domain.model.RecruitmentStatus;
import com.prismworks.prism.domain.project.model.Project;

public class SearchDto {

    @Getter
    @RequiredArgsConstructor
    public static class ProjectSearchRequest {
        private final SearchType searchType;
        private final String searchWord;
        private final Set<Integer> categories;
        private final int pageNo = 0;
        private final int pageSize = 8;

        public ProjectSearchCondition getCondition() {
            return ProjectSearchCondition.builder()
                    .searchType(this.searchType)
                    .searchWord(this.searchWord)
                    .categories(this.categories)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class PostTeamRecruitmentSearchRequest {
        private Set<String> positions;
        private List<String> categories;
        private ProcessMethod processMethod;
        private List<String> skills;
        private RecruitmentStatus recruitmentStatus;
    }

    @Getter
    @RequiredArgsConstructor
    public static class PostTeamRecruitmentSearchResponse {
        private Integer projectId;
        private String projectName;
        private String projectDescription;
        private List<String> categories;
        private List<String> skills;
        private ProcessMethod processMethod;
        private RecruitmentStatus recruitmentStatus;

        public PostTeamRecruitmentSearchResponse(
            Project project,
            ProcessMethod processMethod,
            RecruitmentStatus recruitmentStatus
        ) {
            this.projectId = project.getProjectId();
            this.projectName = project.getProjectName();
            this.projectDescription = project.getProjectDescription();
            this.categories = project.getCategories()
                .stream()
                .map(c -> c.getCategory().getName())
                .collect(Collectors.toList());
            this.skills = project.getSkills();
            this.processMethod = processMethod;
            this.recruitmentStatus = recruitmentStatus;
        }
    }

}

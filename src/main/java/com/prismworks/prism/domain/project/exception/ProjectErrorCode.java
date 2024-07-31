package com.prismworks.prism.domain.project.exception;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import com.prismworks.prism.common.exception.ApplicationErrorCode;
import org.springframework.http.HttpStatus;

public enum ProjectErrorCode implements ApplicationErrorCode {
    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "Project_400_1", "존재하지 않는 프로젝트입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Project_400_2", "존재하지 않는 유저입니다."),
    NO_MEMBER(HttpStatus.BAD_REQUEST, "Project_400_3", "프로젝트 참여 멤버를 입력하세요"),
    NO_PROJECT_NAME(HttpStatus.BAD_REQUEST, "Project_400_4", "프로젝트 명을 입력하세요."),
    NO_DATETIME(HttpStatus.BAD_REQUEST, "Project_400_5", "시작일과 마감일을 입력하세요."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "Project_400_6","존재하지 않는 카테고리입니다."),
    UNAUTHORIZED(HttpStatus.BAD_REQUEST, "Project_400_7","접근 권한이 없는 프로젝트입니다."),
    PEER_REVIEW_TOTAL_RESULT_NOT_EXISTS(HttpStatus.BAD_REQUEST,"Project_404_1","팀원 총평 데이터가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String reason;

    ProjectErrorCode(HttpStatus status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }

    @Override
    public ApiErrorInfo getErrorInfo() {
        return ApiErrorInfo.builder()
                .status(this.status.value())
                .code(this.code)
                .reason(this.reason)
                .build();
    }
}

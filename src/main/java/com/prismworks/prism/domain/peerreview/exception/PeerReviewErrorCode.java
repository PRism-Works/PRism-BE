package com.prismworks.prism.domain.peerreview.exception;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import com.prismworks.prism.common.exception.ApplicationErrorCode;
import org.springframework.http.HttpStatus;

public enum PeerReviewErrorCode implements ApplicationErrorCode {

    ALREADY_FINISH_REVIEW(HttpStatus.BAD_REQUEST, "PeerReviewCode_400_1", "이미 평가를 완료했습니다"),
    REVIEWEE_NOT_EXIST(HttpStatus.BAD_REQUEST, "PeerReviewCode_400_2", "평가할 프로젝트 멤버가 없습니다."),
    UPDATE_PRISM_FORBIDDEN(HttpStatus.BAD_REQUEST, "PeerReviewCode_400_3", "평가 갱신을 할 권한이 없습니다."),
    UPDATE_PRISM_FAILED(HttpStatus.BAD_REQUEST, "PeerReviewCode_400_4", "평가 갱신에 실패했습니다."),
    ALREADY_UPDATE_PRISM(HttpStatus.BAD_REQUEST, "PeerReviewCode_400_5", "이미 평가 갱신이 완료되었습니다"),
    LINK_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "PeerReviewCode_404_1", "평가 링크 코드를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String reason;

    PeerReviewErrorCode(HttpStatus status, String code, String reason) {
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

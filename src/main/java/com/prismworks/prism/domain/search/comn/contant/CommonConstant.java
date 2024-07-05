package com.prismworks.prism.domain.search.comn.contant;

public class CommonConstant {
    public enum CommonKey {
        RESULT_CODE("code"), // 반환 코드
        RESULT_MESSAGE("message"), // 반환 메시지
        RESULT_DATA("data"); // 반환 데이터
        private String value;

        CommonKey(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return value;
        }
    }

    public enum CommonCode {
        SUCCESS("0000"), // 성공
        DUPL("9999"), // 중복
        FAIL("0001"); // 실패
        private String value;

        CommonCode(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return value;
        }
    }

    public enum CommonMessage {
        SUCCESS("success"), // 성공
        FAIL("fail"), // 실패
        AUTHORIZATION_ERROR("authorization error"); // 권한 에러

        private String value;

        CommonMessage(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return value;
        }
    }

    public enum ErrorCode {
        ACCESS_DENIED("접근 불가"),
        NOT_FOUND_USER("회원 찾을 수 없음"),
        NOT_FOUND_DATA("데이터 없음"),
        SEND_EMAIL_ERROR("이메일 발송 에러"),
        TIMEOUT("시간 만료"),
        REQUIRED_VALUE_MISSING("필수값 누락"),
        EXIST("중복"),
        INSERT_FAIL("등록 중 오류가 발생했습니다."),
        UPDATE_FAIL("수정 중 오류가 발생했습니다."),
        SELECT_FAIL("조회 중 오류가 발생했습니다."),
        DELETE_FAIL("삭제 중 오류가 발생했습니다."),
        PROC_FAIL("처리 중 오류가 발생했습니다.");

        private String value;

        ErrorCode(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return value;
        }
    }
}
package com.ewhatever.qna.common.Base;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공했습니다."),

    /**
     * 2000: Request 오류
     */
    // answer(2000-2099)
    ALREADY_JUICY_POST(false, 2000, "이미 쥬시글로 등록된 post입니다,"),
    ALREADY_ANSWERED(false, 2001, "이미 답변한 질문입니다."),

    // comment(2100-2199)
    INVALID_COMMENT_IDX(false, 2100, "잘못된 commentIdx입니다."),
    SHORT_COMMENT_CONTENT(false, 2101, "댓글은 최소 2자여야 합니다."),
    LONG_COMMENT_CONTENT(false, 2102, "댓글은 최대 500자여야 합니다."),

    // notification(2200-2299)

    // post(2300-2399)
    INVALID_CATEGORY(false, 2300, "잘못된 카테고리입니다."),
    INVALID_POST_IDX(false, 2301, "잘못된 postIdx입니다."),

    // user(2400-2499)
    NO_SENIOR_ROLE(false, 2400, "시니가 아닙니다."),
    INVALID_USER(false, 2401, "존재하지 않는 사용자에 대한 요청입니다."),//TODO : IDX 로 수정하기
    NO_JUNIOR_ROLE(false, 2402, "쥬니가 아닙니다."),
    INVALID_TOKEN(false, 2403, "유효한 토큰이 아닙니다"),
    UNMATCHED_REFRESH_TOKEN(false, 2404, "리프레시 토큰이 일치하지 않습니다."),

    // question(2500-2501)
    SHORT_QUESTION_CONTENT(false, 2500, "질문 내용은 최소 10자여야 합니다."),
    LONG_QUESTION_CONTENT(false, 2501, "질문 내용은 최대 1000자여야 합니다."),
    SHORT_QUESTION_TITLE(false, 2502, "질문 제목은 최소 10자여야 합니다."),
    LONG_QUESTION_TITLE(false, 2503, "질문 제목은 최대 50자여야 합니다."),

    // scrap(2600-2699)
    ZERO_SCRAP_COUNT(false, 2600, "이미 스크랩수가 0입니다."),

    /**
     * 3000: Response 오류
     */
    // answer(3000-3099)

    // comment(3100-3199)

    // notification(3200-3299)

    // post(3300-3399)
    NULL_POST(false, 3300, "쥬시글이 없습니다."),

    // user(3400-3499)
    //TODO : 4000번대에 가야하는지 생각해보기
    NAVER_READ_BODY_FAILED(false, 3400, "네이버 회원 정보 조회 API 응답 바디를 읽는데 실패했습니다."),
    NAVER_ACCESS_FAILED(false, 3401, "네이버 회원 프로필 접근에 실패하였습니다."),
    NAVER_ACCESS_TOKEN_FAILED(false, 3402, "네이버 접근 토큰 발급에 실패하였습니다."),

    // question(3500-3599)
    NULL_QUESTION(false, 3500, "질문이 없습니다."),

    // scrap(3600-3699)

    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

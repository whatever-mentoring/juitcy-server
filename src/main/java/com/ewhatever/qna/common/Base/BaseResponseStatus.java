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

    // comment(2100-2199)
    INVALID_COMMENT_IDX(false, 2100, "잘못된 commentIdx입니다."),

    // notification(2200-2299)

    // post(2300-2399)
    INVALID_CATEGORY(false, 2300, "잘못된 카테고리입니다."),
    INVALID_POST_IDX(false, 2301, "잘못된 postIdx입니다."),

    // user(2400-2499)
    NO_SENIOR_ROLE(false, 2400, "시니가 아닙니다."),
    INVALID_USER(false, 2401, "존재하지 않는 사용자에 대한 요청입니다."),//TODO : IDX 로 수정하기
    NO_JUNIOR_ROLE(false, 2402, "쥬니가 아닙니다."),

    // question(2500-2501)

    // scrap(2600-2699)
    ZERO_SCRAP_COUNT(false, 2600, "이미 스크랩수가 0입니다."),

    /**
     * 3000: Response 오류
     */
    // answer(3000-3099)

    // comment(3100-3199)

    // notification(3200-3299)

    // post(3300-3399)
    NULL_POST(false, 3300, "주씨글이 없습니다."),

    // user(3400-3499)

    // question(3500-3599)

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

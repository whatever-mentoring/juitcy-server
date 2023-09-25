package com.ewhatever.qna.common.enums;

public enum Category {
    DAILY("일상"),
    RELATIONSHIP("인간관계"),
    SOCIAL("사회생활"),
    ETC("기타");

    private String krName;

    Category(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return krName;
    }

}


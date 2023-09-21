package com.ewhatever.qna.common.enums;

public enum Role {
    SINY, JUNY;
    private static final String PREFIX = "ROLE_";
    public String getAuthority(){
        return PREFIX + this.name();
    }
}

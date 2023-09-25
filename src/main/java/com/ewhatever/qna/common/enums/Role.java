package com.ewhatever.qna.common.enums;

public enum Role {
    Cyni, Juni;
    private static final String PREFIX = "ROLE_";
    public String getAuthority(){
        return PREFIX + this.name();
    }
}

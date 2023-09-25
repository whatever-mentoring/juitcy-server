package com.ewhatever.qna.login;

public class CustomUnauthorizedException extends RuntimeException {
    public CustomUnauthorizedException() {
        super("Unauthorized");
    }

    public CustomUnauthorizedException(String message) {
        super(message);
    }
}

package com.ewhatever.qna.common.config;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.login.CustomUnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected BaseResponse<?> handleBaseException(BaseException e) {
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler(CustomUnauthorizedException.class)
    protected ResponseEntity<?> handleNoSuchElementFoundException(CustomUnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}

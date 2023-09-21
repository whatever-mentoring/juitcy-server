package com.ewhatever.qna.login.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.common.Base.BaseResponseStatus;
import com.ewhatever.qna.login.dto.LoginRes;
import com.ewhatever.qna.login.service.LoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping(value="/login/naver")
    @ResponseBody
    public BaseResponse<LoginRes> callBack(@RequestParam("code") String code,
                                           @RequestParam("state") String state) throws BaseException, JsonProcessingException {
        return new BaseResponse<>(loginService.callback(code, state));
    }

    @GetMapping("/test")
    @ResponseBody
    public BaseResponse<String> tmp() {
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

}

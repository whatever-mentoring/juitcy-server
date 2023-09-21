package com.ewhatever.qna.user.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.user.dto.GetCommentResponse;
import com.ewhatever.qna.user.dto.GetJunyQuestionResponse;
import com.ewhatever.qna.user.dto.GetScrapResponse;
import com.ewhatever.qna.user.dto.GetSinyAnswerResponse;
import com.ewhatever.qna.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public BaseResponse<?> getProfile(HttpServletRequest request) throws BaseException {
        return userService.getProfile(request.getHeader("Authorization"));
    }


    @GetMapping("/juny/questions")
    public BaseResponse<Page<GetJunyQuestionResponse>> getMyQuestions(HttpServletRequest request,
                                                                      @RequestParam(value = "status", defaultValue = "completed") String status,
                                                                      @RequestParam(value = "requestPageNum", defaultValue = "0") int requestPageNum) throws BaseException {
        return new BaseResponse<>(userService.getMyQuestions(request.getHeader("Authorization"), status, requestPageNum));
    }

    @GetMapping("/siny/answers")
    public BaseResponse<Page<GetSinyAnswerResponse>> getMyAnswers(HttpServletRequest request,
                                                                  @RequestParam(value = "status", defaultValue = "completed") String status,
                                                                  @RequestParam(value = "requestPageNum", defaultValue = "0") int requestPageNum) throws BaseException {
        return new BaseResponse<>(userService.getMyAnswers(request.getHeader("Authorization"), status, requestPageNum));
    }

    @GetMapping("/comments")
    public BaseResponse<Page<GetCommentResponse>> getMyComments(HttpServletRequest request,
                                                                @RequestParam(value = "requestPageNum", defaultValue = "0") int requestPageNum) throws BaseException{
        return new BaseResponse<>(userService.getMyComments(request.getHeader("Authorization"), requestPageNum));
    }

    @GetMapping("/scrap")
    public BaseResponse<Page<GetScrapResponse>> getMyScraps(HttpServletRequest request,
                                                            @RequestParam(value = "requestPageNum", defaultValue = "0") int requestPageNum) throws BaseException{
        return new BaseResponse<>(userService.getMyScraps(request.getHeader("Authorization"), requestPageNum));
    }
}

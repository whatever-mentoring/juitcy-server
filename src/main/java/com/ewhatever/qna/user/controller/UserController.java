package com.ewhatever.qna.user.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.user.dto.GetSubscriptionRes;
import com.ewhatever.qna.user.dto.PostSubscriptionReq;
import com.ewhatever.qna.user.dto.GetCommentResponse;
import com.ewhatever.qna.user.dto.GetJunyQuestionResponse;
import com.ewhatever.qna.user.dto.GetScrapResponse;
import com.ewhatever.qna.user.dto.GetSinyAnswerResponse;
import com.ewhatever.qna.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

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


    @ResponseBody
    @PostMapping("/subscription")
    public BaseResponse<String> subscribeLetter(HttpServletRequest request,
                                                @Valid @RequestBody PostSubscriptionReq postSubscriptionReq,
                                                BindingResult bindingResult) throws BaseException {
        userService.subscribeLetter(request.getHeader("Authorization"), postSubscriptionReq, bindingResult);
        return new BaseResponse<>(SUCCESS);
    }

    @ResponseBody
    @DeleteMapping("/subscription")
    public BaseResponse<String> deleteSubscriptionLetter(HttpServletRequest request) throws BaseException {
        userService.deleteSubscriptionLetter(request.getHeader("Authorization"));
        return new BaseResponse<>(SUCCESS);
    }

    @ResponseBody
    @GetMapping("/subscription")
    public BaseResponse<GetSubscriptionRes> getSubscriptionInformation(HttpServletRequest request) throws BaseException {
        return new BaseResponse<>(userService.getSubscriptionInformation(request.getHeader("Authorization")));
    }
}

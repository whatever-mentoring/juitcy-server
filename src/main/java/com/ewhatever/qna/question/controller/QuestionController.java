package com.ewhatever.qna.question.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.question.dto.PostQuestionReq;
import com.ewhatever.qna.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    QuestionService questionService;

    /**
     * 질문 등록
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> addAnswer(@RequestBody PostQuestionReq postQuestionReq, Long userIdx) {
        try {
            questionService.addQuestion(userIdx, postQuestionReq); //TODO: authService.getUserIdx()로 수정
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

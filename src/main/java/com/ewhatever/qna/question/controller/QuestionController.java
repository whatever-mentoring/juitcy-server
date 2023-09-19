package com.ewhatever.qna.question.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.question.dto.GetQuestionsRes;
import com.ewhatever.qna.question.dto.PostQuestionReq;
import com.ewhatever.qna.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    /**
     * [POST] 질문 등록
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> addAnswer(@RequestBody PostQuestionReq postQuestionReq, Long userIdx) {
        try {
            questionService.addQuestion(postQuestionReq); //TODO: authService.getUserIdx()로 수정
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [GET] 질문 목록 조회
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<Page<GetQuestionsRes>> getQuestions(@RequestParam(required = false) String category, Pageable page) {
        try {
            if (category.isBlank()) { // 전체 조회
                return new BaseResponse<>(questionService.getQuestions(page));
            } else return new BaseResponse<>(questionService.getQuestionsByCategory(category, page)); // 카테고리 조회
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

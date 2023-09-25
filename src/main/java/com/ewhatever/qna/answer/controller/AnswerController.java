package com.ewhatever.qna.answer.controller;

import com.ewhatever.qna.answer.dto.PostAnswerReq;
import com.ewhatever.qna.answer.service.AnswerService;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    /**
     * 답변 등록
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> addAnswer(HttpServletRequest request, @RequestBody PostAnswerReq postAnswerReq) {
        try {
            answerService.addAnswer(request.getHeader("Authorization"), postAnswerReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

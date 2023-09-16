package com.ewhatever.qna.answer.controller;

import com.ewhatever.qna.answer.dto.PostAnswerReq;
import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.answer.service.AnswerService;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    AnswerService answerService;
    /**
     * 답변 등록
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> addAnswer(@RequestBody PostAnswerReq postAnswerReq, Long userIdx) {
        try {
            answerService.addAnswer(userIdx, postAnswerReq); //TODO: authService.getUserIdx()로 수정
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

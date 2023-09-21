package com.ewhatever.qna.answer.dto;

import lombok.Data;

@Data
public class PostAnswerReq {
    private String answer;
    private Long postIdx;
}

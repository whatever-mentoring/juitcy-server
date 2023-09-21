package com.ewhatever.qna.question.dto;

import lombok.Data;

@Data
public class PostQuestionReq {
    private String title;
    private String category;
    private String content;
}

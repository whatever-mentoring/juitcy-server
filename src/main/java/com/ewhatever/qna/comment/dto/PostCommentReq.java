package com.ewhatever.qna.comment.dto;

import lombok.Getter;

@Getter
public class PostCommentReq {
    private Long postIdx;
    private String content;
}

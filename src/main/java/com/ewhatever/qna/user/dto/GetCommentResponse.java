package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class GetCommentResponse {
    private String content;
    private LocalDate createdDate;
    private Long postIdx;
    private String postTitle;

    //TODO : DTO 변환 메소드 위치 변경
    //TODO : dto에서 LocalDateTime -> LocalDate로 변경
    public static GetCommentResponse fromComment(Comment comment) {
        return GetCommentResponse.builder().content(comment.getContent().substring(0, 16))
                .createdDate(comment.getCreatedDate().toLocalDate())
                .postIdx(comment.getPost().getPostIdx())
                .postTitle(comment.getPost().getTitle()).build();
    }
}

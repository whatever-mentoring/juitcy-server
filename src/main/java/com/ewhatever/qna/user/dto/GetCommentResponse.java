package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.post.entity.Post;
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
        Post post = comment.getPost();
        return GetCommentResponse.builder().content(comment.getContent().substring(0, Math.min(comment.getContent().length(), 20)))
                .createdDate(comment.getCreatedDate().toLocalDate())//TODO : 날짜 포맷 수정 -> 시간 필요
                .postIdx(post.getPostIdx())
                .postTitle(post.getTitle().substring(0, Math.min(post.getTitle().length(), 10))).build();
    }
}

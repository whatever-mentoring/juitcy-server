package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
public class GetCommentResponse {
    private String content;
    private String date;
    private Long postIdx;
    private String postTitle;

    //TODO : DTO 변환 메소드 위치 변경
    //TODO : dto에서 LocalDateTime -> LocalDate로 변경
    public static GetCommentResponse fromComment(Comment comment) {
        Post post = comment.getPost();

        String title;
        if(post.getTitle().length() > 10) title = post.getTitle().substring(0, 10) + "...";
        else title = post.getTitle();

        String content;
        if(comment.getContent().length() > 20) content = comment.getContent().substring(0, 20) + "...";
        else content = comment.getContent();

        return GetCommentResponse.builder().content(content)
                .date(comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")))
                .postIdx(post.getPostIdx())
                .postTitle(title).build();
    }
}

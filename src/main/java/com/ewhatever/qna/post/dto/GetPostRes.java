package com.ewhatever.qna.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostRes {
    private String category;
    private List<String> cardList; // 질문 제목, 질문 상세, 답변(0-3개)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime date;
    private Long commentCount;
    private Long scrapCount;
    private Boolean isScrap;
    private List<CommentDto> commentList;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private String writer;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime date;
        private String content;
        private Boolean isWriter;
    }
}

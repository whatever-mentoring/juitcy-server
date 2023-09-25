package com.ewhatever.qna.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostRes {
    private String category;
    private List<String> cardList; // 질문 제목, 질문 상세, 답변(0-3개)
    @JsonIgnore
    private LocalDateTime date;
    private Long commentCount;
    private Long scrapCount;
    private Boolean isScrap;
    private List<CommentDto> commentList;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Long commentIdx;
        private String writer;
        @JsonIgnore
        private LocalDateTime date;
        private String content;
        private Boolean isWriter;

        @JsonProperty("commentDateTime")
        public String getShortYearDate() {
            if (date != null) {
                return date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
            } else {
                return null;
            }
        }
    }

    @JsonProperty("juicyDateTime")
    public String getShortYearDate() {
        if (date != null) {
            return date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
        } else {
            return null;
        }
    }
}

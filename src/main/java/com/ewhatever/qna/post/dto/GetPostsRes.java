package com.ewhatever.qna.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsRes {
    private String category;
    @JsonIgnore
    private LocalDateTime date;
    private Long scrapCount;
    private Long commentCount;
    private List<String> cardList; // 질문 제목, 질문 상세, 답변(0-3개)

    @JsonProperty("juicyDateTime")
    public String getShortYearDate() {
        if (date != null) {
            return date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
        } else {
            return null;
        }
    }
}

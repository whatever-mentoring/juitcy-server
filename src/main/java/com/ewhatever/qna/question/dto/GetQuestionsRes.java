package com.ewhatever.qna.question.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class GetQuestionsRes {
    private String category;
    private String title;
    private String content;
    @JsonIgnore
    private LocalDateTime date;

    @JsonProperty("dateTime")
    public String getShortYearDate() {
        if (date != null) {
            return date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
        } else {
            return null;
        }
    }
}

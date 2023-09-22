package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.common.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@Setter
@Getter
public class GetSinyAnswerResponse {
    private String content;
    private String date;
    private String category;
    private Long answerCount;
    private Long daysUntilDday;

    public static GetSinyAnswerResponse fromAnswer(Answer answer, Long answerCount, Boolean isJuicy) {
        LocalDate targetDate = answer.getPost().getCreatedDate().toLocalDate().plusDays(3);
        LocalDateTime date = (isJuicy)? answer.getPost().getLastModifiedDate() : answer.getPost().getCreatedDate();
        return GetSinyAnswerResponse.builder()
                    .content(answer.getContent().substring(0, Math.min(20, answer.getContent().length())))
                    .date(date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")))
                    .category(answer.getPost().getCategory().getKrName())
                    .answerCount(answerCount)
                    .daysUntilDday(LocalDate.now().until(targetDate, DAYS)).build();
    }
}

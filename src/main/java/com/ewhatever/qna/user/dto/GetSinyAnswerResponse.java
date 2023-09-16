package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.common.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@Setter
@Getter
public class GetSinyAnswerResponse {
    private String content;
    private LocalDateTime date;
    private Category category;
    private Long answerCount;
    private Long daysUntilDday;

    public static GetSinyAnswerResponse fromAnswer(Answer answer, Long answerCount, Boolean isJuicy) {
        LocalDate targetDate = answer.getPost().getCreatedDate().toLocalDate().plusDays(3);
        LocalDateTime date = (isJuicy)? answer.getPost().getLastModifiedDate() : answer.getPost().getCreatedDate();
        return GetSinyAnswerResponse.builder()
                    .content(answer.getContent().substring(0, 16))
                    .date(date)
                    .category(answer.getPost().getCategory())
                    .answerCount(answerCount)
                    .daysUntilDday(LocalDate.now().until(targetDate, DAYS)).build();
    }
}

package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@Getter
@Setter
public class GetJunyQuestionResponse {
    private String title;
    private LocalDateTime date;
    private Category category;
    private Long answerCount;
    private Long daysUntilDday;

    public static GetJunyQuestionResponse fromPost(Post post, Long answerCount, Boolean isJuicy) {
        LocalDate targetDate = post.getCreatedDate().toLocalDate().plusDays(3);
        LocalDateTime date = (isJuicy)? post.getLastModifiedDate() : post.getCreatedDate();
        return GetJunyQuestionResponse.builder()
                .title(post.getTitle().substring(0, Math.min(post.getTitle().length(), 20)))
                .date(date)//TODO : 날짜 포맷 수정
                .category(post.getCategory())
                .answerCount(answerCount)
                .daysUntilDday(LocalDate.now().until(targetDate, DAYS)).build();
    }
}

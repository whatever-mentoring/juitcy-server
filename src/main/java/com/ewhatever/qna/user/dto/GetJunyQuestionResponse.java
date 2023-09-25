package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@Getter
@Setter
public class GetJunyQuestionResponse {
    private String title;
    private String date;
    private String category;
    private Long answerCount;
    private Long daysUntilDday;
    private Long postIdx;

    public static GetJunyQuestionResponse fromPost(Post post, Long answerCount, Boolean isJuicy) {
        LocalDate targetDate = post.getCreatedDate().toLocalDate().plusDays(3);
        LocalDateTime date = (isJuicy)? post.getJuicyDate() : post.getCreatedDate();
        String title;
        if(post.getTitle().length() > 22)
            title = post.getTitle().substring(0, 22) + "...";
        else title = post.getTitle();
        return GetJunyQuestionResponse.builder()
                .title(title)
                .postIdx(post.getPostIdx())
                .date(date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")))
                .category(post.getCategory().getKrName())
                .answerCount(answerCount)
                .daysUntilDday(LocalDate.now().until(targetDate, DAYS)).build();
    }
}

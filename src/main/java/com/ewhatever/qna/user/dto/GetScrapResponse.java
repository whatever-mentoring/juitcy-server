package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.scrap.entity.Scrap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Setter
@Getter
public class GetScrapResponse {
    private String postTitle;
    private Long postIdx;
    private String category;
    private String content;
    private Long commentCount;
    private Long scrapCount;
    private String date;//쥬시글 등록 날짜
    //TODO : 해당 필드명 수정하기??

    //TODO : substring말고 잘라진 부분만 가져오게 수정?
    public static GetScrapResponse fromScrap(Scrap scrap) {
        Post post = scrap.getPost();
        String title;
        if(post.getTitle().length() > 21) title = post.getTitle().substring(0, 21) + "...";
        else title = post.getTitle();

        String content;
        if(post.getContent().length() > 58)
            content = post.getContent().substring(0, 30) + "\n" + post.getContent().substring(30, 58) + "...";
        else if(post.getContent().length() > 30)
            content = post.getContent().substring(0, 30) + "\n" + post.getContent().substring(30);
        else content = post.getContent();

        return GetScrapResponse.builder().postTitle(title)
                .postIdx(post.getPostIdx())
                .category(post.getCategory().getKrName())
                .content(content)
                .commentCount(post.getCommentCount())
                .scrapCount(post.getScrapCount())
                .date(post.getJuicyDate().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")))
                .build();
    }
}

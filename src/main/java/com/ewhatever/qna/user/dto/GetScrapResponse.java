package com.ewhatever.qna.user.dto;

import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.scrap.entity.Scrap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class GetScrapResponse {
    private String postTitle;
    private Long postIdx;
    private Category category;
    private String content;
    private Long commentCount;
    private Long scrapCount;
    private LocalDateTime createdDate;//쥬시글 등록 날짜
    //TODO : 해당 필드명 수정하기??

    //TODO : substring말고 잘라진 부분만 가져오게 수정?
    public static GetScrapResponse fromScrap(Scrap scrap) {
        Post post = scrap.getPost();
        return GetScrapResponse.builder().postTitle(post.getTitle().substring(0, Math.min(post.getTitle().length(), 10)))
                .postIdx(post.getPostIdx())
                .category(post.getCategory())
                .content(post.getContent().substring(0, Math.min(post.getContent().length(), 45)))
                .commentCount(post.getCommentCount())
                .scrapCount(post.getScrapCount())
                .createdDate(post.getLastModifiedDate()).build();//TODO : 날짜 포맷 수정
    }
}

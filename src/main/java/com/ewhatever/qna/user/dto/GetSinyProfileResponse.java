package com.ewhatever.qna.user.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetSinyProfileResponse {
    private String name;
    private Long answerCount;
    private Long commentCount;
    private Long scrapCount;
}

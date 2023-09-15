package com.ewhatever.qna.user.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetJunyProfileResponse {
    private String name;
    private Long questionCount;
    private Long commentCount;
    private Long scrapCount;
}

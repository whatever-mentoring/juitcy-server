package com.ewhatever.qna.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetRefreshedAccessTokenRes {

    @JsonProperty("access_token")
    private String accessToken;

    private Long exp;
}

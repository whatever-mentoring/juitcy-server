package com.ewhatever.qna.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenDto {
    @Setter
    @JsonProperty("access_token")
    private String accessToken;

    @Setter
    @JsonProperty("refresh_token")
    private String refreshToken;
}

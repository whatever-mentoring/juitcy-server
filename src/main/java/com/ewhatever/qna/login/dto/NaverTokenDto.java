package com.ewhatever.qna.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@JsonPropertyOrder({"access_token", "refresh_token", "token_type", "expires_in", "error", "error_description"})
public class NaverTokenDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private String expiredIn;

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;



}

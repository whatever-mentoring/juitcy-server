package com.ewhatever.qna.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@JsonPropertyOrder({"access_token", "result", "expires_in", "error", "error_description"})
public class DeleteNaverTokenRes {

    @JsonProperty("access_token")
    private String accessToken;//삭제 처리된 접근 토큰 값

    @JsonProperty("result")
    private String result;//처리 결과가 성공이면 'success'가 리턴

    @JsonProperty("expires_in")
    private int expiredIn;//접근 토큰의 유효 기간(초 단위)

    @JsonProperty("error")
    private String error;//에러 코드

    @JsonProperty("error_description")
    private String errorDescription;//에러 메시지

}

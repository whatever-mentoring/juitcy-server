package com.ewhatever.qna.login.dto;

import com.ewhatever.qna.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Jwt;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {

    @JsonProperty("access_token")
    private String accessToken;
    private Long exp;
    private Boolean isNewUser;
    private String role;
}

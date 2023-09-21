package com.ewhatever.qna.login.dto;

import com.ewhatever.qna.common.enums.Role;
import io.jsonwebtoken.Jwt;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    private JwtTokenDto jwt;
    private Boolean isNewUser;
    private String role;
}

package com.ewhatever.qna.login.dto;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.login.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtIssuer jwtIssuer;

    public Long getUserIdx(String token) throws BaseException {
        return Long.parseLong(jwtIssuer.getClaims(token).getSubject());
    }
}

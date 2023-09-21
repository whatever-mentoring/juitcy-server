package com.ewhatever.qna.login;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.login.dto.JwtTokenDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider {

    //private final UserDetailsService userDetailsService;
    private final JwtIssuer jwtIssuer;

    // 인증용
    public boolean validateToken(String token) throws BaseException {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = jwtIssuer.getClaims(token);
        if (claims == null) {
            return false;
        }

        /*
         * 추가 검증 로직
         */

        return true;
    }

    // 재발급용
    public boolean validateToken(JwtTokenDto jwtDto) throws BaseException {
        if (!StringUtils.hasText(jwtDto.getAccessToken())
                || !StringUtils.hasText(jwtDto.getRefreshToken())) {
            return false;
        }

        Claims accessClaims = jwtIssuer.getClaims(jwtDto.getAccessToken());
        Claims refreshClaims = jwtIssuer.getClaims(jwtDto.getRefreshToken());

        /*
         * 추가 검증 로직
         */

        return accessClaims != null && refreshClaims != null
                && jwtIssuer.getSubject(accessClaims).equals(jwtIssuer.getSubject(refreshClaims));
    }


    //TODO : userIdxStr 수정
    /*
    public Authentication getAuthentication(String token) throws BaseException {
        Claims claims = jwtIssuer.getClaims(token);
        String userIdxStr = jwtIssuer.getSubject(claims);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userIdxStr);

        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }*/

}

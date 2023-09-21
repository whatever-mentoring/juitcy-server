package com.ewhatever.qna.login;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponseStatus;
import com.ewhatever.qna.login.dto.JwtTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private static String SECRET_KEY = "secretKeyForJsonWebTokenTutorial";
    public static final long EXPIRE_TIME = 1000 * 60 * 5;
    public static final long REFRESH_EXPIRE_TIME = 1000 * 60 * 15;
    public static final String ROLE = "role";

    @PostConstruct
    void init(){
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public JwtTokenDto createToken(Long userIdx, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userIdx));
        claims.put(ROLE, role);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String getSubject(Claims claims) {
        return claims.getSubject();
    }

    public Claims getClaims(String token) throws BaseException {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            //throw new BadCredentialsException("유효한 토큰이 아닙니다.");
            throw new BaseException(BaseResponseStatus.NO_VALID_TOKEN);
        }
        return claims;
    }

}

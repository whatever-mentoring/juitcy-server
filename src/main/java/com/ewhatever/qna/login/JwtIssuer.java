package com.ewhatever.qna.login;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponseStatus;
import com.ewhatever.qna.login.dto.GetRefreshedAccessTokenRes;
import com.ewhatever.qna.login.dto.JwtTokenDto;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtIssuer {

    private static String SECRET_KEY = "secretKeyForJsonWebTokenTutorial";
    public static final long EXPIRE_TIME = 1000 * 60 * 30;//30분 // TODO : 만료시간
    public static final long REFRESH_EXPIRE_TIME = 1000 * 60 * 360;//6시간 //TODO : 만료시간 재설정
    public static final String ROLE = "role";

    @PostConstruct
    void init(){
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public JwtTokenDto createTokens(Long userIdx, String role) throws BaseException {

        String accessToken = this.createToken(userIdx, role, EXPIRE_TIME);

        String refreshToken = this.createToken(userIdx, role, REFRESH_EXPIRE_TIME);

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .exp(this.getClaims(accessToken).get("exp", Date.class).getTime()/1000)
                .build();
    }
    public GetRefreshedAccessTokenRes createRefreshedAccessToken(Long userIdx, String role) throws BaseException {
        String accessToken = this.createToken(userIdx, role, EXPIRE_TIME);

        return GetRefreshedAccessTokenRes.builder()
                .accessToken(accessToken)
                .exp(this.getClaims(accessToken).get("exp", Date.class).getTime()/1000)
                .build();
    }

    public String createToken(Long userIdx, String role, long exp) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userIdx));
        claims.put(ROLE, role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
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
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
        }
        return claims;
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.SignatureException | MalformedJwtException e) {
            throw new CustomUnauthorizedException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new CustomUnauthorizedException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new CustomUnauthorizedException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new CustomUnauthorizedException("JWT 토큰이 잘못되었습니다.");
        }
    }

}

package com.ewhatever.qna.login.service;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.login.JwtIssuer;
import com.ewhatever.qna.login.dto.*;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtIssuer jwtIssuer;

    private final AuthService authService;

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String clientId = "mp0CQ1x6hXjAn1PDkrgT";
    private final String clientSecret = "yZJwVrevKp";

    private final String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
    private final String tokenUrl =  "https://nid.naver.com/oauth2.0/token";

    public String getAccessToken(String authorizationCode, String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("grant_type", "authorization_code");
        params.set("client_id", clientId);
        params.set("client_secret", clientSecret);
        params.set("code", authorizationCode);
        params.set("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(tokenUrl, request, String.class);

        try {
            return objectMapper.readValue(response.getBody(), NaverTokenDto.class).getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //public LoginRes callback(HttpServletRequest request) throws BaseException, JsonProcessingException
    public LoginRes callback(String code, String state) throws BaseException, JsonProcessingException {

        String token = getAccessToken(code, state);
        String header = "Bearer " + token; // Bearer 다음에 공백 추가

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(userInfoUrl,requestHeaders);
        GetNaverProfileRes getNaverProfileRes = objectMapper.readValue(responseBody, GetNaverProfileRes.class);
        System.out.println(responseBody);
        System.out.println(getNaverProfileRes.getResponse().toString());


        System.out.println(getNaverProfileRes.getResultcode());
        if(getNaverProfileRes != null && getNaverProfileRes.getResultcode().equals("00")) { // 요청 성공
            return createUser(getNaverProfileRes, token);
        }
        else throw new BaseException(NAVER_ACCESS_FAILED);
    }

    @Transactional
    public void logout(String token) throws Exception {
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        DeleteNaverTokenRes deleteNaverTokenRes = deleteNaverToken(user.getProviderToken());
        if(!deleteNaverTokenRes.getResult().equals("success")) throw new Exception("토큰 삭제 실패");//TODO : Exception 수정
        else {
            user.setProviderToken(null);
            user.setRefreshToken(null);
        }
    }

    public DeleteNaverTokenRes deleteNaverToken(String accessToken) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("grant_type", "delete");
        params.set("client_id", clientId);
        params.set("client_secret", clientSecret);

        //UnsupportedEncodingException
        params.set("access_token", URLEncoder.encode(accessToken, "UTF-8"));//네이버 토큰 갱신, 삭제시 액세스 토큰 url 인코딩 필요
        params.set("service_provider", "NAVER");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(tokenUrl, request, String.class);

        try {
            //JsonProcessingException
            DeleteNaverTokenRes deleteNaverTokenRes = objectMapper.readValue(response.getBody(), DeleteNaverTokenRes.class);
            System.out.println(deleteNaverTokenRes.toString());
            return deleteNaverTokenRes;
        } catch (Exception e) {
            e.printStackTrace();//TODO : Exception 처리
        }
        return null;
    }

    @Transactional
    public LoginRes createUser(GetNaverProfileRes getNaverProfileRes, String providerAccessToken) throws BaseException {
        Boolean isNewUser = true;
        User user;

        Optional<User> optionalUser = userRepository.findByNaverIdAndStatusEquals(getNaverProfileRes.getResponse().getId(), ACTIVE);

        if(optionalUser.isPresent()) {
            isNewUser = false;//기존 가입 회원
            user = optionalUser.get();
        }
        else user = userRepository.save(getNaverProfileRes.getResponse().toEntity());

        user.setProviderToken(providerAccessToken);
        JwtTokenDto jwtTokenDto = jwtIssuer.createToken(user.getUserIdx(), user.getRole().name());
        user.setRefreshToken(jwtTokenDto.getRefreshToken());
        userRepository.save(user);

        System.out.println(authService.getUserIdx(jwtTokenDto.getAccessToken()));
        Date exp = jwtIssuer.getClaims(jwtTokenDto.getAccessToken()).get("exp", Date.class);
        return LoginRes.builder().accessToken(jwtTokenDto.getAccessToken()).exp(exp.getTime()/1000).isNewUser(isNewUser).role(user.getRole().name()).build();

    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){

        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                log.info("*** response : [{}]", con.getInputStream());
                return readBody(con.getInputStream());
            } else { // 에러 발생
                log.info("*** response : [{}]", con.getInputStream());
                return readBody(con.getErrorStream());
            }
        } catch (IOException | BaseException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);//TODO : BaseResponseStatus에 추가
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);//TODO : BaseResponseStatus에 추가
        }
    }


    private static String readBody(InputStream body) throws BaseException, UnsupportedEncodingException {

        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new BaseException(NAVER_READ_BODY_FAILED);
        }
    }
}

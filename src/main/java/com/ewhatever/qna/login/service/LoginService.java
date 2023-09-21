package com.ewhatever.qna.login.service;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.login.JwtIssuer;
import com.ewhatever.qna.login.dto.*;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            return createUser(getNaverProfileRes);
        }
        else throw new BaseException(NAVER_ACCESS_FAILED);
    }

    @Transactional
    public LoginRes createUser(GetNaverProfileRes getNaverProfileRes) throws BaseException {
        Boolean isNewUser = true;
        String roleStr;
        JwtTokenDto jwtTokenDto;
        Optional<User> optionalUser = userRepository.findByNaverIdAndStatusEquals(getNaverProfileRes.getResponse().getId(), ACTIVE);
        if(optionalUser.isPresent()) {
            isNewUser = false;//기존 가입 회원
            roleStr = optionalUser.get().getRole().name();
            jwtTokenDto = jwtIssuer.createToken(optionalUser.get().getUserIdx(), roleStr);
        }
        else {
            User user = userRepository.save(getNaverProfileRes.getResponse().toEntity());
            roleStr = user.getRole().name();
            jwtTokenDto = jwtIssuer.createToken(user.getUserIdx(), roleStr);
        }

        System.out.println(authService.getUserIdx(jwtTokenDto.getAccessToken()));

        return LoginRes.builder().jwt(jwtTokenDto).isNewUser(isNewUser).role(roleStr).build();

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

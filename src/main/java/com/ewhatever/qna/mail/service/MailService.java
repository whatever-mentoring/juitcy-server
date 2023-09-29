package com.ewhatever.qna.mail.service;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.login.CustomUnauthorizedException;
import com.ewhatever.qna.login.JwtIssuer;
import com.ewhatever.qna.login.dto.AuthService;
import com.ewhatever.qna.mail.dto.GetSubscriptionRes;
import com.ewhatever.qna.mail.dto.PostMailReq;
import com.ewhatever.qna.mail.dto.PostSubscriptionReq;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    private final JwtIssuer jwtIssuer;

    private final UserRepository userRepository;

    private final AuthService authService;

    @Async
    public void sendMail(PostMailReq postMailReq) throws BaseException {

        log.info("*** 메일 전송 시작 ***");

        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            //메일 제목 설정
            helper.setSubject(postMailReq.getTitle());

            List<String> emails = userRepository.findEmailByStatus(ACTIVE);
            log.info("*** 메일 발송 대상 : {}", emails.toString());
            helper.setTo(emails.toArray(new String[0]));

            //템플릿에 전달할 데이터 설정
            HashMap<String, String> emailValues = new HashMap<>();
            emailValues.put("title", postMailReq.getContentTitle());
            emailValues.put("content", postMailReq.getContent());


            Context context = new Context();
            emailValues.forEach((key, value)->{
                context.setVariable(key, value);
            });

            //메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process("juiceLetter", context);
            helper.setText(html, true);

            //메일 보내기
            emailSender.send(message);
        } catch(MessagingException e) {
            throw new BaseException(MAIL_DELIVERY_ERROR);
        }

            log.info("*** 메일 전송 완료 ***");
    }

    @Transactional
    public void subscribeLetter(String token, PostSubscriptionReq postSubscriptionReq, BindingResult bindingResult) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        if(bindingResult.hasErrors()) throw new BaseException(INVALID_EMAIL);
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        user.setEmail(postSubscriptionReq.getEmail());
    }

    @Transactional
    public void deleteSubscriptionLetter(String token) throws BaseException {
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        user.setEmail(null);
    }

    public GetSubscriptionRes getSubscriptionInformation(String token) throws BaseException {
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        return new GetSubscriptionRes(user);
    }
}
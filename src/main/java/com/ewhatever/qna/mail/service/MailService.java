package com.ewhatever.qna.mail.service;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.mail.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.MAIL_DELIVERY_ERROR;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(MailDto mailDto) throws BaseException {

        log.info("*** 메일 전송 시작 ***");

        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            //메일 제목 설정
            helper.setSubject(mailDto.getTitle());

            //TODO : DB에서 읽어와서 나중에 구독한 사람들로 설정하기
            helper.setTo(new String[]{"hahaha329@ewhain.net"});

            //템플릿에 전달할 데이터 설정
            HashMap<String, String> emailValues = new HashMap<>();
            emailValues.put("title", mailDto.getContentTitle());
            emailValues.put("content", mailDto.getContent());


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
}
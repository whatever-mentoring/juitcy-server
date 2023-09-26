package com.ewhatever.qna.mail;

import com.ewhatever.qna.comment.dto.PostCommentReq;
import com.ewhatever.qna.comment.service.CommentService;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.mail.dto.MailDto;
import com.ewhatever.qna.mail.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RequestMapping("/letter")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping
    public BaseResponse<String> addComment(@RequestBody MailDto mailDto) throws BaseException {
        mailService.sendMail(mailDto);
        return new BaseResponse<>(SUCCESS);
    }

}

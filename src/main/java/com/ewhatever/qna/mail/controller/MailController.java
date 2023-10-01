package com.ewhatever.qna.mail.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.mail.dto.PostMailReq;
import com.ewhatever.qna.mail.service.MailService;
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
    public BaseResponse<String> sendMail(@RequestBody PostMailReq postMailReq) throws BaseException {
        mailService.sendMail(postMailReq);
        return new BaseResponse<>(SUCCESS);
    }

}

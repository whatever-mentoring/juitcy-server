package com.ewhatever.qna.mail;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.mail.dto.GetSubscriptionRes;
import com.ewhatever.qna.mail.dto.PostMailReq;
import com.ewhatever.qna.mail.dto.PostSubscriptionReq;
import com.ewhatever.qna.mail.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
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

    @ResponseBody
    @PostMapping("/subscription")
    public BaseResponse<String> subscribeLetter(HttpServletRequest request,
                                                @Valid @RequestBody PostSubscriptionReq postSubscriptionReq,
                                                BindingResult bindingResult) throws BaseException {
        mailService.subscribeLetter(request.getHeader("Authorization"), postSubscriptionReq, bindingResult);
        return new BaseResponse<>(SUCCESS);
    }

    @ResponseBody
    @DeleteMapping("/subscription")
    public BaseResponse<String> deleteSubscriptionLetter(HttpServletRequest request) throws BaseException {
        mailService.deleteSubscriptionLetter(request.getHeader("Authorization"));
        return new BaseResponse<>(SUCCESS);
    }

    @ResponseBody
    @GetMapping("/subscription")
    public BaseResponse<GetSubscriptionRes> getSubscriptionInformation(HttpServletRequest request) throws BaseException {
        return new BaseResponse<>(mailService.getSubscriptionInformation(request.getHeader("Authorization")));
    }

}

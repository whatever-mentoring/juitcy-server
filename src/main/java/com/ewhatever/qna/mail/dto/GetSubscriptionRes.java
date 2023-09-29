package com.ewhatever.qna.mail.dto;

import com.ewhatever.qna.user.entity.User;
import lombok.Getter;

@Getter
public class GetSubscriptionRes {
    private Boolean isSubscribed;
    private String email;

    public GetSubscriptionRes(User user) {
        if(user.getEmail() != null) //null 값이 아니면 구독중
        {
            this.isSubscribed = true;
            this.email = user.getEmail();
        }
        else this.isSubscribed = false;
    }
}

package com.ewhatever.qna.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostSubscriptionReq {

    @Email
    @NotNull
    private String email;
}

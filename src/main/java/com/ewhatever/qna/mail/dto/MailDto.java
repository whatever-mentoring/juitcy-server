package com.ewhatever.qna.mail.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailDto {
    private String title;
    private String contentTitle;
    private String content;
}

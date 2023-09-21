package com.ewhatever.qna.login.dto;

import com.ewhatever.qna.common.enums.Role;
import com.ewhatever.qna.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonPropertyOrder({"id", "name", "birthday"})
public class UserInfoDto {

    @JsonProperty("name")
    private String name;
    @JsonProperty("birthyear")
    private String birthyear;

    @JsonProperty("id")
    private String id;//중복 확인을 위한 유니크 String

    public User toEntity() {
        Role role = (Integer.valueOf(this.birthyear) > LocalDate.now().getYear() - 49)? Role.JUNY : Role.SINY;
        return User.builder().name(this.name).provider("naver").role(role).naverId(this.id).build();
    }
}

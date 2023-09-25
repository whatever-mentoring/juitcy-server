package com.ewhatever.qna.user.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import com.ewhatever.qna.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 30)
    private String name;
    private String refreshToken;//jwt refreshToken

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @NotNull
    private String naverId;//TODO : VARCHAR 크기 수정

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
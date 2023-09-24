package com.ewhatever.qna.user.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import com.ewhatever.qna.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Collections;

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
    @Enumerated(value = EnumType.ORDINAL)//TODO : EnumType 수정
    private Role role;

    @NotNull
    private String naverId;//TODO : VARCHAR 크기 수정

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /*
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.getAuthority()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return String.valueOf(userIdx);
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }*/
}
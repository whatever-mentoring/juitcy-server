package com.ewhatever.qna.user.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    private String provider;

    @NotNull
    @Size(max = 30)
    private String role;

}
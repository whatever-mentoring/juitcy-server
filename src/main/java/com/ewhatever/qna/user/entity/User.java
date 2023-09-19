package com.ewhatever.qna.user.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import com.ewhatever.qna.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
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
    @Enumerated(value = EnumType.ORDINAL)//TODO : EnumType 수정
    private Role role;

    @NotNull
    private String naverId;//TODO : VARCHAR 크기 수정
}
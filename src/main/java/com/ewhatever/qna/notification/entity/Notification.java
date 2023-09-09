package com.ewhatever.qna.notification.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import com.ewhatever.qna.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationIdx;

    @NotNull
    @Column(length = 150)
    private String message;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @NotNull
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isChecked = false;
}

package com.ewhatever.qna.post.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    @NotNull
    @Size(max = 300)
    private String title;

    @NotNull
    @Size(min = 30, max = 3000)
    private String content;

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    private Category category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questioner_idx")
    private User questioner;

    @NotNull
    @ColumnDefault("0")
    @Builder.Default
    private Long scrapCount = 0L;

    @NotNull
    @ColumnDefault("0")
    @Builder.Default
    private Long commentCount = 0L;

    @NotNull
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isJuicy = false;
}

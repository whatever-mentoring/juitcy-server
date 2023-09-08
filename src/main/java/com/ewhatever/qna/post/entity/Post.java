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

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    @NotNull
    @Size(min = 30, max = 1000)
    private String question;

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
}

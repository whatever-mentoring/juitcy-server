package com.ewhatever.qna.answer.entity;

import com.ewhatever.qna.common.BaseEntity;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerIdx;

    @NotNull
    @Size(min = 30, max = 1000)
    private String content;

    @NotNull
    @JoinColumn(name = "answerer_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private User answerer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}

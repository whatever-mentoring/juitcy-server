package com.ewhatever.qna.comment.entity;

import com.ewhatever.qna.common.Base.BaseEntity;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @NotNull
    @Size(max = 500)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private Post post;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_idx")
    private User writer;

    public void setContent(String content) {
        this.content = content;
    }
}

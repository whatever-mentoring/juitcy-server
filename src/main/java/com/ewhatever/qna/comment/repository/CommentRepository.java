package com.ewhatever.qna.comment.repository;

import com.ewhatever.qna.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

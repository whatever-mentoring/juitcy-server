package com.ewhatever.qna.comment.repository;

import com.ewhatever.qna.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByWriter_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Comment> findByWriter_UserIdxAndStatusEquals(Long userIdx, String status, Pageable pageable);
}

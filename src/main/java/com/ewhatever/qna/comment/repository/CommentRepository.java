package com.ewhatever.qna.comment.repository;

import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByWriter_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Comment> findByWriter_UserIdxAndStatusEquals(Long userIdx, String status, Pageable pageable);
    List<Comment> findAllByPostAndStatusEquals(Post post, String status);
}

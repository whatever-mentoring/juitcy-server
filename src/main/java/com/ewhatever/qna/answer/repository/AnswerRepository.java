package com.ewhatever.qna.answer.repository;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByPost(Post post);
    Long countByAnswerer_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Answer> findByAnswerer_UserIdxAndPost_IsJuicyTrueAndStatusEquals(Long userIdx, String status, Pageable pageable);
    Page<Answer> findByAnswerer_UserIdxAndPost_IsJuicyFalseAndStatusEquals(Long userIdx, String status, Pageable pageable);
    Long countByPost_PostIdxAndStatusEquals(Long postIdx, String status);
    int countByPostAndIsJuicyFalse(Post post);
}
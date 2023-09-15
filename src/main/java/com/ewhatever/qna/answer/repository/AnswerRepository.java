package com.ewhatever.qna.answer.repository;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByPost(Post post);
}
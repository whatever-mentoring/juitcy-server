package com.ewhatever.qna.answer.repository;

import com.ewhatever.qna.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
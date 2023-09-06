package com.ewhatever.qna.post.repository;

import com.ewhatever.qna.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

package com.ewhatever.qna.post.repository;

import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsJuicy(Pageable pageable, Boolean isJuicy);
    Boolean existsByCategory(Category category);
    Page<Post> findByCategory(Category category, Pageable pageable);
    Long countByQuestioner_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Post> findByQuestioner_UserIdxAndIsJuicyAndStatusEquals(Long userIdx, Boolean isJuicy, String status, Pageable pageable);
}

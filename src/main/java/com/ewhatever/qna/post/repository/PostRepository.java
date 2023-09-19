package com.ewhatever.qna.post.repository;

import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsJuicyTrue(Pageable pageable);
    Page<Post> findAllByIsJuicyFalse(Pageable pageable);
    Boolean existsByCategoryAndIsJuicyFalse(Category category);
    Boolean existsByCategoryAndIsJuicyTrue(Category category);
    Page<Post> findAllByCategoryAndIsJuicyTrue(Category category, Pageable pageable);
    Page<Post> findAllByCategoryAndIsJuicyFalse(Category category, Pageable pageable);
    Long countByQuestioner_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Post> findByQuestioner_UserIdxAndIsJuicyTrueAndStatusEquals(Long userIdx, String status, Pageable pageable);
    Page<Post> findByQuestioner_UserIdxAndIsJuicyFalseAndStatusEquals(Long userIdx, String status, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE (p.title LIKE CONCAT('%', :searchWord, '%') OR p.content LIKE CONCAT('%', :searchWord, '%')) AND p.isJuicy = true")
    Page<Post> searchJuicyPosts(@Param("searchWord") String searchWord, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Post p WHERE (p.title LIKE CONCAT('%', :searchWord, '%') OR p.content LIKE CONCAT('%', :searchWord, '%')) AND p.isJuicy = true")
    boolean existsJuicyPosts(@Param("searchWord") String searchWord);
}

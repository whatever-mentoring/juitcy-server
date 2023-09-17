package com.ewhatever.qna.scrap.repository;

import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.scrap.entity.Scrap;
import com.ewhatever.qna.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Long countByUser_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Scrap> findByUser_UserIdxAndStatusEquals(Long userIdx, String status, Pageable pageable);
    Boolean existsByPostAndUserAndStatusEquals(Post post, User user, String status);
}

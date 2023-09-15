package com.ewhatever.qna.scrap.repository;

import com.ewhatever.qna.scrap.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Long countByUser_UserIdxAndStatusEquals(Long userIdx, String status);
    Page<Scrap> findByUser_UserIdxAndStatusEquals(Long userIdx, String status, Pageable pageable);
}

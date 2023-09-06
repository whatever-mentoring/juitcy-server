package com.ewhatever.qna.scrap.repository;

import com.ewhatever.qna.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
}

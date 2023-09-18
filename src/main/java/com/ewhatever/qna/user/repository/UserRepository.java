package com.ewhatever.qna.user.repository;

import com.ewhatever.qna.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdxAndStatusEquals(Long userIdx, String status);
}

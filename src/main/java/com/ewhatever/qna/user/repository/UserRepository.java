package com.ewhatever.qna.user.repository;

import com.ewhatever.qna.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdxAndStatusEquals(Long userIdx, String status);
    Optional<User> findByNaverIdAndStatusEquals(String naverId, String status);
    @Query("select u.email from User u where u.email is not null and u.status = :status")
    List<String> findEmailByStatus(@Param("status") String status);
}

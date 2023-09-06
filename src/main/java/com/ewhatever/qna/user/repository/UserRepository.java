package com.ewhatever.qna.user.repository;

import com.ewhatever.qna.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

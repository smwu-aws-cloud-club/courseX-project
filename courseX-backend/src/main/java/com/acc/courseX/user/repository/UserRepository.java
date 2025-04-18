package com.acc.courseX.user.repository;

import static com.acc.courseX.user.code.UserFailure.NOT_FOUND_USER;

import com.acc.courseX.user.entity.User;
import com.acc.courseX.user.exception.UserException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  default User findByIdOrThrow(Long userId) {
    return findById(userId).orElseThrow(() -> new UserException(NOT_FOUND_USER));
  }
}

package com.acc.courseX.user.repository;

import static com.acc.courseX.user.code.UserFailure.NOT_FOUND_USER;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import com.acc.courseX.user.entity.User;
import com.acc.courseX.user.exception.UserException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT u FROM User u WHERE u.id = :id")
  Optional<User> findByIdForUpdate(@Param("id") Long id);

  default User findByIdOrThrow(Long userId) {
    return findById(userId).orElseThrow(() -> new UserException(NOT_FOUND_USER));
  }

  default User findByIdForUpdateOrThrow(Long userId) {
    return findByIdForUpdate(userId).orElseThrow(() -> new UserException(NOT_FOUND_USER));
  }
}

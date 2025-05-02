package com.acc.courseX.log.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.acc.courseX.log.entity.Log;
import com.acc.courseX.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Long> {
  List<Log> findByUser(User user);

  List<Log> findByActionType(String actionType);

  List<Log> findByTargetTable(String targetTable);

  List<Log> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  @Query(
      "SELECT l FROM Log l WHERE "
          + "(:userId IS NULL OR l.user.id = :userId) AND "
          + "(:actionType IS NULL OR l.actionType = :actionType) AND "
          + "(:targetTable IS NULL OR l.targetTable = :targetTable) AND "
          + "(:startDate IS NULL OR l.createdAt >= :startDate) AND "
          + "(:endDate IS NULL OR l.createdAt <= :endDate) "
          + "ORDER BY l.createdAt DESC")
  List<Log> findWithFilters(
      @Param("userId") Long userId,
      @Param("actionType") String actionType,
      @Param("targetTable") String targetTable,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);
}

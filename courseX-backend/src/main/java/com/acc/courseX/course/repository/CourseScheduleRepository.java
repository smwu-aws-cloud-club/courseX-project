package com.acc.courseX.course.repository;

import java.time.LocalTime;

import com.acc.courseX.course.entity.CourseSchedule;
import com.acc.courseX.course.entity.Weekday;
import com.acc.courseX.enrollment.entity.EnrollmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
  @Query(
      "SELECT COUNT(cs1) > 0 "
          + "FROM CourseSchedule cs1 "
          + "JOIN Enrollment e ON cs1.course.id = e.course.id "
          + "JOIN User u ON e.user.id = u.id "
          + "WHERE u.id = :userId "
          + "AND e.status = :enrollmentStatus "
          + "AND cs1.weekday = :weekday "
          + "AND cs1.startTime < :newEndTime "
          + "AND cs1.endTime > :newStartTime")
  boolean existsTimeOverlap(
      @Param("userId") Long userId,
      @Param("enrollmentStatus") EnrollmentStatus status,
      @Param("weekday") Weekday weekday,
      @Param("newStartTime") LocalTime newStartTime,
      @Param("newEndTime") LocalTime newEndTime);
}

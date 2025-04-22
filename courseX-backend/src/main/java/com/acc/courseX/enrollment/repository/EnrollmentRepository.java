package com.acc.courseX.enrollment.repository;

import java.time.LocalTime;
import java.util.List;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.entity.Weekday;
import com.acc.courseX.enrollment.entity.Enrollment;
import com.acc.courseX.enrollment.entity.EnrollmentStatus;
import com.acc.courseX.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  boolean existsByCourseAndUser(Course course, User user);

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

  @Query(
      "SELECT DISTINCT e "
          + "FROM Enrollment e "
          + "JOIN FETCH e.course c "
          + "JOIN FETCH c.professor "
          + "LEFT JOIN FETCH c.schedules "
          + "WHERE e.user.id = :userId")
  List<Enrollment> findAllByUserIdWithCourseDetails(@Param("userId") Long userId);
}

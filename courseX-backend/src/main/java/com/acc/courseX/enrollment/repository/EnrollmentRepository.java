package com.acc.courseX.enrollment.repository;

import java.util.List;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.enrollment.entity.Enrollment;
import com.acc.courseX.enrollment.entity.EnrollmentStatus;
import com.acc.courseX.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  boolean existsByCourseAndUserAndStatus(Course course, User user, EnrollmentStatus status);

  @Query(
      "SELECT DISTINCT e "
          + "FROM Enrollment e "
          + "JOIN FETCH e.course c "
          + "JOIN FETCH c.professor "
          + "LEFT JOIN FETCH c.schedules "
          + "WHERE e.user.id = :userId AND e.status = :status")
  List<Enrollment> findAllByUserIdAndStatusWithCourseDetails(
      @Param("userId") Long userId, @Param("status") EnrollmentStatus status);
}

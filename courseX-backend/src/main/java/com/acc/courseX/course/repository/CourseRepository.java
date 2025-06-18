package com.acc.courseX.course.repository;

import static com.acc.courseX.course.code.CourseFailure.NOT_FOUND_COURSE;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.exception.CourseException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query(
      "SELECT DISTINCT c "
          + "FROM Course c "
          + "JOIN FETCH c.professor "
          + "LEFT JOIN FETCH c.schedules")
  List<Course> findAllWithProfessorAndSchedules();

  @Query(
      "SELECT DISTINCT c "
          + "FROM Course c "
          + "JOIN FETCH c.professor "
          + "LEFT JOIN FETCH c.schedules "
          + "WHERE c.code = :code")
  List<Course> findAllByCodeWithProfessorAndSchedules(@Param("code") String code);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Course c WHERE c.id = :id")
  Optional<Course> findByIdForUpdate(@Param("id") Long id);

  default Course findByIdForUpdateOrThrow(Long courseId) {
    return findByIdForUpdate(courseId).orElseThrow(() -> new CourseException(NOT_FOUND_COURSE));
  }
}

package com.acc.courseX.course.repository;

import static com.acc.courseX.course.code.CourseFailure.NOT_FOUND_COURSE;

import java.util.List;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.exception.CourseException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
  List<Course> findByCode(String code);

  default Course findByIdOrThrow(Long courseId) {
    return findById(courseId).orElseThrow(() -> new CourseException(NOT_FOUND_COURSE));
  }
}

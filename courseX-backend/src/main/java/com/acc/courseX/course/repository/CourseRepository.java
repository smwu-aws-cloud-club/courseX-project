package com.acc.courseX.course.repository;

import java.util.List;

import com.acc.courseX.course.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
  List<Course> findByCode(String code);
}

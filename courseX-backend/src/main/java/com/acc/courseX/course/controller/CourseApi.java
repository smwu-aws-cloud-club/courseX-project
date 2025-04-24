package com.acc.courseX.course.controller;

import org.springframework.http.ResponseEntity;

public interface CourseApi {
  ResponseEntity<?> getCourses(String code);

  ResponseEntity<?> enroll(Long courseId, Long userId);
}

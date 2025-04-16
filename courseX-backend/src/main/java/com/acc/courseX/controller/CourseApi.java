package com.acc.courseX.controller;

import org.springframework.http.ResponseEntity;

public interface CourseApi {
  ResponseEntity<?> getCourses(String code);
}

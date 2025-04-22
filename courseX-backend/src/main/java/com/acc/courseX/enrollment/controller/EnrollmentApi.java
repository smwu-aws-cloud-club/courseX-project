package com.acc.courseX.enrollment.controller;

import org.springframework.http.ResponseEntity;

public interface EnrollmentApi {
  ResponseEntity<?> getEnrollments(Long userId);
}

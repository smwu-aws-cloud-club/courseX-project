package com.acc.courseX.enrollment.controller;

import com.acc.courseX.common.annotation.AuthUserId;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController implements EnrollmentApi {
  @GetMapping
  @Override
  public ResponseEntity<?> getEnrollments(@AuthUserId Long userId) {
    return null;
  }
}

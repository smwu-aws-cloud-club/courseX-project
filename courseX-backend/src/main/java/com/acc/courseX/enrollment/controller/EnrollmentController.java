package com.acc.courseX.enrollment.controller;

import static com.acc.courseX.enrollment.code.EnrollmentSuccess.GET_ENROLLMENT_LIST;

import java.util.List;

import com.acc.courseX.common.annotation.AuthUserId;
import com.acc.courseX.common.response.ResponseUtil;
import com.acc.courseX.enrollment.dto.EnrollmentResponse;
import com.acc.courseX.enrollment.service.EnrollmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController implements EnrollmentApi {
  private final EnrollmentService enrollmentService;

  @GetMapping
  @Override
  public ResponseEntity<?> getEnrollments(@AuthUserId Long userId) {
    List<EnrollmentResponse> response = enrollmentService.getEnrollments(userId);

    return ResponseUtil.success(GET_ENROLLMENT_LIST, response);
  }
}

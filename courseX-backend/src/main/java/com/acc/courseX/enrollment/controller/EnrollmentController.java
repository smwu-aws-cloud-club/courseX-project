package com.acc.courseX.enrollment.controller;

import static com.acc.courseX.course.code.CourseSuccess.COURSE_ENROLLMENT_CANCEL_SUCCESS;

import com.acc.courseX.common.annotation.AuthUserId;
import com.acc.courseX.common.response.ResponseUtil;
import com.acc.courseX.enrollment.service.EnrollmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController implements EnrollmentApi {
  private final EnrollmentService enrollmentService;

  @DeleteMapping("/{enrollmentId}")
  @Override
  public ResponseEntity<?> cancel(@PathVariable Long enrollmentId, @AuthUserId Long userId) {
    enrollmentService.cancel(enrollmentId, userId);
    return ResponseUtil.success(COURSE_ENROLLMENT_CANCEL_SUCCESS);
  }
}

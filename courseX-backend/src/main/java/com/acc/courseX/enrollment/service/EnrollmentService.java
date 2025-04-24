package com.acc.courseX.enrollment.service;

import java.util.List;

import com.acc.courseX.enrollment.dto.EnrollmentResponse;

public interface EnrollmentService {
  List<EnrollmentResponse> getEnrollments(Long userId);

  void cancel(Long enrollmentId, Long userId);
}

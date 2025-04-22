package com.acc.courseX.enrollment.service;

import java.util.List;

import com.acc.courseX.enrollment.dto.EnrollmentResponse;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;

  @Transactional(readOnly = true)
  @Override
  public List<EnrollmentResponse> getEnrollments(Long userId) {
    return null;
  }
}

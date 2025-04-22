package com.acc.courseX.enrollment.service;

import java.util.List;

import com.acc.courseX.enrollment.dto.EnrollmentResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
  @Override
  public List<EnrollmentResponse> getEnrollments(Long userId) {
    return null;
  }
}

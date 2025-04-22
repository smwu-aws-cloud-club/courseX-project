package com.acc.courseX.enrollment.service;

import java.util.List;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.enrollment.dto.EnrollmentResponse;
import com.acc.courseX.enrollment.entity.Enrollment;
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
    List<Enrollment> enrollments = enrollmentRepository.findAllByUserIdWithCourseDetails(userId);

    return enrollments.stream()
        .map(
            enrollment -> {
              Course course = enrollment.getCourse();
              return EnrollmentResponse.of(
                  enrollment.getId(),
                  course.getCode(),
                  course.getName(),
                  course.getCredit(),
                  course.getProfessorName(),
                  course.getCourseSchedule());
            })
        .toList();
  }
}

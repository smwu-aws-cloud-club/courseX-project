package com.acc.courseX.enrollment.service;

import static com.acc.courseX.enrollment.code.EnrollmentFailure.ENROLLMENT_ALREADY_CANCELLED;
import static com.acc.courseX.enrollment.code.EnrollmentFailure.ENROLLMENT_NOT_FOUND;
import static com.acc.courseX.enrollment.code.EnrollmentFailure.UNAUTHORIZED_CANCEL;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.enrollment.entity.Enrollment;
import com.acc.courseX.enrollment.entity.EnrollmentStatus;
import com.acc.courseX.enrollment.exception.EnrollmentException;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;

  @Override
  @Transactional
  public void cancel(Long enrollmentId, Long userId) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(enrollmentId)
            .orElseThrow(() -> new EnrollmentException(ENROLLMENT_NOT_FOUND));

    if (!enrollment.getUser().getId().equals(userId)) {
      throw new EnrollmentException(UNAUTHORIZED_CANCEL);
    }

    if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
      throw new EnrollmentException(ENROLLMENT_ALREADY_CANCELLED);
    }

    enrollment.cancel();

    Course course = enrollment.getCourse();
    course.decreaseCurrentStudents();
  }
}

package com.acc.courseX.enrollment.validator;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.user.entity.User;

import org.springframework.stereotype.Component;

@Component
public class GeneralCourseEnrollmentValidator extends AbstractEnrollmentValidator {

  public GeneralCourseEnrollmentValidator(EnrollmentRepository enrollmentRepository) {
    super(enrollmentRepository);
  }

  @Override
  protected void validateCourseSpecificRules(Course course, User user) {}
}

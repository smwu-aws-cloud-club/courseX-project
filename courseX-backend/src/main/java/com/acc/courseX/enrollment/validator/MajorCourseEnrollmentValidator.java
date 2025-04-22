package com.acc.courseX.enrollment.validator;

import static com.acc.courseX.enrollment.code.EnrollmentFailure.MAJOR_MISMATCH;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.repository.CourseScheduleRepository;
import com.acc.courseX.enrollment.exception.EnrollmentException;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.user.entity.User;

import org.springframework.stereotype.Component;

@Component
public class MajorCourseEnrollmentValidator extends AbstractEnrollmentValidator {

  public MajorCourseEnrollmentValidator(
      EnrollmentRepository enrollmentRepository,
      CourseScheduleRepository courseScheduleRepository) {
    super(enrollmentRepository, courseScheduleRepository);
  }

  @Override
  protected void validateCourseSpecificRules(Course course, User user) {
    boolean isMajorMismatch = !course.getProfessor().getMajor().equals(user.getMajor());

    if (isMajorMismatch) {
      throw new EnrollmentException(MAJOR_MISMATCH);
    }
  }
}

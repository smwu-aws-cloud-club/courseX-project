package com.acc.courseX.enrollment.validator;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.entity.CourseType;

import org.springframework.stereotype.Component;

@Component
public class EnrollmentValidatorFactory {

  private final MajorCourseEnrollmentValidator majorValidator;
  private final GeneralCourseEnrollmentValidator generalValidator;

  public EnrollmentValidatorFactory(
      MajorCourseEnrollmentValidator majorValidator,
      GeneralCourseEnrollmentValidator generalValidator) {
    this.majorValidator = majorValidator;
    this.generalValidator = generalValidator;
  }

  public EnrollmentValidator getValidator(Course course) {
    return course.getCourseType() == CourseType.MAJOR ? majorValidator : generalValidator;
  }
}

package com.acc.courseX.enrollment.validator;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.user.entity.User;

public interface EnrollmentValidator {
  void validate(Course course, User user);
}

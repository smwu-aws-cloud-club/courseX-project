package com.acc.courseX.enrollment.validator;

import static com.acc.courseX.course.code.CourseFailure.COURSE_FULL;
import static com.acc.courseX.enrollment.code.EnrollmentFailure.ALREADY_ENROLLED;
import static com.acc.courseX.enrollment.code.EnrollmentFailure.TIMETABLE_CONFLICT;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.entity.CourseSchedule;
import com.acc.courseX.course.exception.CourseException;
import com.acc.courseX.enrollment.exception.EnrollmentException;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractEnrollmentValidator implements EnrollmentValidator {

  private final EnrollmentRepository enrollmentRepository;

  @Override
  public void validate(Course course, User user) {
    validateAlreadyEnrolled(course, user);
    validateCourseSpecificRules(course, user);
    validateRemainingSeats(course);
    validateTimetableConflict(course, user);
  }

  protected void validateAlreadyEnrolled(Course course, User user) {
    if (enrollmentRepository.existsByCourseAndUser(course, user)) {
      throw new EnrollmentException(ALREADY_ENROLLED);
    }
  }

  protected void validateRemainingSeats(Course course) {
    if (course.getRemainingSeats() <= 0) {
      throw new CourseException(COURSE_FULL);
    }
  }

  protected void validateTimetableConflict(Course course, User user) {
    for (CourseSchedule newSchedule : course.getSchedules()) {
      boolean isConflict =
          enrollmentRepository.existsTimeOverlap(
              user.getId(),
              newSchedule.getWeekday(),
              newSchedule.getStartTime(),
              newSchedule.getEndTime());

      if (isConflict) {
        throw new EnrollmentException(TIMETABLE_CONFLICT);
      }
    }
  }

  protected abstract void validateCourseSpecificRules(Course course, User user);
}

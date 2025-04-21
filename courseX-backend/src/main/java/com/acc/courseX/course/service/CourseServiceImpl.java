package com.acc.courseX.course.service;

import static com.acc.courseX.enrollment.code.EnrollmentFailure.*;

import java.util.List;
import java.util.stream.Collectors;

import com.acc.courseX.course.dto.CourseResponse;
import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.repository.CourseRepository;
import com.acc.courseX.enrollment.entity.Enrollment;
import com.acc.courseX.enrollment.entity.EnrollmentStatus;
import com.acc.courseX.enrollment.exception.EnrollmentException;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.enrollment.validator.EnrollmentValidator;
import com.acc.courseX.enrollment.validator.EnrollmentValidatorFactory;
import com.acc.courseX.user.entity.User;
import com.acc.courseX.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;
  private final EnrollmentValidatorFactory enrollmentValidatorFactory;
  private final EnrollmentRepository enrollmentRepository;

  @Transactional(readOnly = true)
  @Override
  public List<CourseResponse> getCourses(final String code) {
    boolean hasCodeFilter = code != null && !code.isBlank();

    List<Course> courses =
        hasCodeFilter ? courseRepository.findByCode(code) : courseRepository.findAll();

    return courses.stream()
        .map(
            course ->
                CourseResponse.of(
                    course.getId(),
                    course.getCode(),
                    course.getName(),
                    course.getCredit(),
                    course.getProfessorName(),
                    course.getCourseSchedule(),
                    course.getMaxStudents(),
                    course.getRemainingSeats()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void enroll(Long courseId, Long userId) {
    User user = userRepository.findByIdOrThrow(userId);
    Course course = courseRepository.findByIdOrThrow(courseId);
    EnrollmentValidator validator = enrollmentValidatorFactory.getValidator(course);

    validator.validate(course, user);
    Enrollment newEnrollment = Enrollment.builder().course(course).user(user).build();
    enrollmentRepository.save(newEnrollment);
    course.increaseCurrentStudents();
  }

  @Override
  @Transactional
  public void cancelEnrollment(Long enrollmentId, Long userId) {
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

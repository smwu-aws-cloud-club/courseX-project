package com.acc.courseX.course.service;

import static com.acc.courseX.enrollment.code.EnrollmentFailure.ALREADY_ENROLLED;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        hasCodeFilter
            ? courseRepository.findAllByCodeWithProfessorAndSchedules(code)
            : courseRepository.findAllWithProfessorAndSchedules();

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
    executeWithRetry(() -> enrollWithLock(courseId, userId), courseId, userId);
  }

  private void enrollWithLock(Long courseId, Long userId) {
    User user = userRepository.findByIdForUpdateOrThrow(userId);
    Course course = courseRepository.findByIdForUpdateOrThrow(courseId);

    if (enrollmentRepository.existsByCourseAndUserAndStatus(
        course, user, EnrollmentStatus.ENROLLED)) {
      throw new EnrollmentException(ALREADY_ENROLLED);
    }

    EnrollmentValidator validator = enrollmentValidatorFactory.getValidator(course);
    validator.validate(course, user);

    Enrollment newEnrollment = Enrollment.builder().course(course).user(user).build();
    enrollmentRepository.save(newEnrollment);
    course.increaseCurrentStudents();
  }

  private void executeWithRetry(Runnable operation, Long courseId, Long userId) {
    int maxRetries = 3;
    int retryCount = 0;

    while (retryCount < maxRetries) {
      try {
        operation.run();
        return;
      } catch (CannotAcquireLockException | ObjectOptimisticLockingFailureException e) {
        retryCount++;
        log.warn(
            "Deadlock or lock conflict occurred during enrollment. Retry attempt: {}/{}",
            retryCount,
            maxRetries);

        if (retryCount >= maxRetries) {
          log.error(
              "Failed to enroll after {} retries. CourseId: {}, UserId: {}",
              maxRetries,
              courseId,
              userId);
          throw e;
        }

        waitBeforeRetry(retryCount);
      }
    }
  }

  private void waitBeforeRetry(int retryCount) {
    try {
      long waitTime = (long) Math.pow(2, retryCount) * 100;
      Thread.sleep(waitTime);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Enrollment retry was interrupted. Retry count: {}", retryCount);
      throw new RuntimeException("Enrollment retry was interrupted", e);
    }
  }
}

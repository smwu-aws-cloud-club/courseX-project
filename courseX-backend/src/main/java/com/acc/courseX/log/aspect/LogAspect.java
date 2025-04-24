package com.acc.courseX.log.aspect;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.repository.CourseRepository;
import com.acc.courseX.enrollment.entity.Enrollment;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.log.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogAspect {
  private final LogService logService;
  private final ObjectMapper objectMapper;
  private final EnrollmentRepository enrollmentRepository;
  private final CourseRepository courseRepository;

  @AfterReturning("execution(* com.acc.courseX.course.controller.CourseController.getCourses(..))")
  public void logAfterViewCourse(JoinPoint joinPoint) {
    try {
      Object[] args = joinPoint.getArgs();
      String code = (String) args[0];

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      Long userId = extractUserIdFromRequest(request);
      if (userId == null) return;

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("courseCode", code);

      logService.createLog(
          userId,
          "view_course",
          "courses",
          null,
          objectMapper.writeValueAsString(metadata),
          request);
    } catch (Exception e) {
      log.error("Failed to log view course action", e);
    }
  }

  @AfterReturning("execution(* com.acc.courseX.course.controller.CourseController.enroll(..))")
  public void logAfterEnrollment(JoinPoint joinPoint) {
    try {
      Object[] args = joinPoint.getArgs();
      Long courseId = (Long) args[0];
      Long userId = (Long) args[1];

      Course course = courseRepository.findById(courseId).orElse(null);

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      if (course != null) {
        metadata.put("courseName", course.getName());
        metadata.put("courseCode", course.getCode());
      }

      logService.createLog(
          userId,
          "enroll_course",
          "enrollments",
          courseId,
          objectMapper.writeValueAsString(metadata),
          request);
    } catch (Exception e) {
      log.error("Failed to log enrollment action", e);
    }
  }

  @AfterThrowing(
      pointcut = "execution(* com.acc.courseX.course.controller.CourseController.enroll(..))",
      throwing = "exception")
  public void logAfterEnrollmentFailure(JoinPoint joinPoint, Exception exception) {
    try {
      Object[] args = joinPoint.getArgs();
      Long courseId = (Long) args[0];
      Long userId = (Long) args[1];

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("errorMessage", exception.getMessage());
      metadata.put("exceptionType", exception.getClass().getSimpleName());

      logService.createLog(
          userId,
          "enroll_course_failure",
          "courses",
          courseId,
          objectMapper.writeValueAsString(metadata),
          request);
    } catch (Exception e) {
      log.error("Failed to log enrollment failure action", e);
    }
  }

  @AfterReturning(
      "execution(* com.acc.courseX.enrollment.controller.EnrollmentController.cancel(..))")
  public void logAfterCancelEnrollment(JoinPoint joinPoint) {
    try {
      Object[] args = joinPoint.getArgs();
      Long enrollmentId = (Long) args[0];
      Long userId = (Long) args[1];

      Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      if (enrollment != null) {
        metadata.put("courseId", enrollment.getCourse().getId());
      }

      logService.createLog(
          userId,
          "drop_course",
          "enrollments",
          enrollmentId,
          objectMapper.writeValueAsString(metadata),
          request);
    } catch (Exception e) {
      log.error("Failed to log cancel enrollment action", e);
    }
  }

  @AfterThrowing(
      pointcut =
          "execution(* com.acc.courseX.enrollment.controller.EnrollmentController.cancel(..))",
      throwing = "exception")
  public void logAfterCancelEnrollmentFailure(JoinPoint joinPoint, Exception exception) {
    try {
      Object[] args = joinPoint.getArgs();
      Long enrollmentId = (Long) args[0];
      Long userId = (Long) args[1];

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("errorMessage", exception.getMessage());
      metadata.put("exceptionType", exception.getClass().getSimpleName());

      logService.createLog(
          userId,
          "drop_course_failure",
          "enrollments",
          enrollmentId,
          objectMapper.writeValueAsString(metadata),
          request);
    } catch (Exception e) {
      log.error("Failed to log cancel enrollment failure action", e);
    }
  }

  private Long extractUserIdFromRequest(HttpServletRequest request) {
    String userId = request.getHeader("X-User-Id");
    return userId != null ? Long.parseLong(userId) : null;
  }
}

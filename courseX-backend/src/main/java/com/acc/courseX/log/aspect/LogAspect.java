package com.acc.courseX.log.aspect;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

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

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("courseId", courseId);

      logService.createLog(
          userId,
          "enroll_course",
          "enrollments",
          null,
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
      metadata.put("courseId", courseId);
      metadata.put("errorMessage", exception.getMessage());
      metadata.put("exceptionType", exception.getClass().getSimpleName());

      logService.createLog(
          userId,
          "enroll_course_failure",
          "enrollments",
          null,
          objectMapper.writeValueAsString(metadata),
          request);
    } catch (Exception e) {
      log.error("Failed to log enrollment failure action", e);
    }
  }

  @AfterReturning(
      "execution(* com.acc.courseX.course.controller.CourseController.cancelEnrollment(..))")
  public void logAfterCancelEnrollment(JoinPoint joinPoint) {
    try {
      Object[] args = joinPoint.getArgs();
      Long enrollmentId = (Long) args[0];
      Long userId = (Long) args[1];

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("enrollmentId", enrollmentId);

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
          "execution(* com.acc.courseX.course.controller.CourseController.cancelEnrollment(..))",
      throwing = "exception")
  public void logAfterCancelEnrollmentFailure(JoinPoint joinPoint, Exception exception) {
    try {
      Object[] args = joinPoint.getArgs();
      Long enrollmentId = (Long) args[0];
      Long userId = (Long) args[1];

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("enrollmentId", enrollmentId);
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

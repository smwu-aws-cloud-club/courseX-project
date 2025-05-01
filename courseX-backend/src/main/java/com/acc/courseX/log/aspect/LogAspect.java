package com.acc.courseX.log.aspect;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.repository.CourseRepository;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.log.entity.LogAction;
import com.acc.courseX.log.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
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
    String courseCode = (String) joinPoint.getArgs()[0];
    Long userId = extractUserIdOrDefault(0L);
    Map<String, Object> metadata = Map.of("courseCode", courseCode);
    logAction(userId, LogAction.VIEW_COURSE, "courses", null, metadata);
  }

  @AfterReturning("execution(* com.acc.courseX.course.controller.CourseController.enroll(..))")
  public void logAfterEnrollment(JoinPoint joinPoint) {
    Long courseId = (Long) joinPoint.getArgs()[0];
    Long userId = (Long) joinPoint.getArgs()[1];

    Course course = courseRepository.findById(courseId).orElse(null);
    Map<String, Object> metadata = new HashMap<>();
    if (course != null) {
      metadata.put("courseName", course.getName());
      metadata.put("courseCode", course.getCode());
    }

    logAction(userId, LogAction.ENROLL_COURSE, "enrollments", courseId, metadata);
  }

  @AfterThrowing(
      pointcut = "execution(* com.acc.courseX.course.controller.CourseController.enroll(..))",
      throwing = "exception")
  public void logAfterEnrollmentFailure(JoinPoint joinPoint, Exception exception) {
    Long courseId = (Long) joinPoint.getArgs()[0];
    Long userId = (Long) joinPoint.getArgs()[1];

    Map<String, Object> metadata = errorMetadata(exception);
    logAction(userId, LogAction.ENROLL_COURSE_FAILURE, "courses", courseId, metadata);
  }

  @AfterReturning(
      "execution(* com.acc.courseX.enrollment.controller.EnrollmentController.cancel(..))")
  public void logAfterCancelEnrollment(JoinPoint joinPoint) {
    Long enrollmentId = (Long) joinPoint.getArgs()[0];
    Long userId = (Long) joinPoint.getArgs()[1];

    Map<String, Object> metadata = new HashMap<>();

    logAction(userId, LogAction.DROP_COURSE, "enrollments", enrollmentId, metadata);
  }

  @AfterThrowing(
      pointcut =
          "execution(* com.acc.courseX.enrollment.controller.EnrollmentController.cancel(..))",
      throwing = "exception")
  public void logAfterCancelEnrollmentFailure(JoinPoint joinPoint, Exception exception) {
    Long enrollmentId = (Long) joinPoint.getArgs()[0];
    Long userId = (Long) joinPoint.getArgs()[1];

    Map<String, Object> metadata = errorMetadata(exception);
    logAction(userId, LogAction.DROP_COURSE_FAILURE, "enrollments", enrollmentId, metadata);
  }

  @Around("execution(* com.acc.courseX.log.service.LogService.*(..))")
  public Object measureLogServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed(); // 메서드 실행
    long executionTime = System.currentTimeMillis() - startTime;

    log.info(
        "LogService method [{}] executed in {} ms",
        joinPoint.getSignature().toShortString(),
        executionTime);

    return result;
  }

  private void logAction(
      Long userId,
      LogAction action,
      String targetTable,
      Long targetId,
      Map<String, Object> metadata) {
    try {
      HttpServletRequest request = getCurrentRequest();
      String metaJson = objectMapper.writeValueAsString(metadata != null ? metadata : Map.of());
      logService.createLog(
          userId, action.getActionName(), targetTable, targetId, metaJson, request);
    } catch (Exception e) {
      log.error("Failed to create log for action: {}", action, e);
    }
  }

  private Map<String, Object> errorMetadata(Exception e) {
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("errorMessage", e.getMessage());
    metadata.put("exceptionType", e.getClass().getSimpleName());
    return metadata;
  }

  private Long extractUserIdOrDefault(Long defaultValue) {
    Long id = extractUserId();
    return id != null ? id : defaultValue;
  }

  private Long extractUserId() {
    HttpServletRequest request = getCurrentRequest();
    String userIdStr = request.getHeader("X-User-Id");
    try {
      return userIdStr != null ? Long.parseLong(userIdStr) : null;
    } catch (NumberFormatException e) {
      log.warn("Invalid userId format in header: {}", userIdStr);
      return null;
    }
  }

  private HttpServletRequest getCurrentRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
  }
}

package com.acc.courseX.enrollment.dto;

public record EnrollmentResponse(
    Long enrollmentId,
    String courseCode,
    String courseName,
    int courseCredit,
    String courseProfessorName,
    String courseSchedule) {
  public static EnrollmentResponse of(
      Long enrollmentId,
      String courseCode,
      String courseName,
      int courseCredit,
      String courseProfessorName,
      String courseSchedule) {
    return new EnrollmentResponse(
        enrollmentId, courseCode, courseName, courseCredit, courseProfessorName, courseSchedule);
  }
}

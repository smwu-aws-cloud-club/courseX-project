package com.acc.courseX.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CourseResponse(
    @NotNull Long courseId,
    @NotNull String code,
    @NotNull String name,
    @Min(1) int credit,
    @NotNull String professorName,
    @NotNull String courseSchedule,
    @Min(1) int maxStudent,
    @Min(0) int remainingSeats) {
  public static CourseResponse of(
      Long courseId,
      String code,
      String name,
      int credit,
      String professorName,
      String courseSchedule,
      int maxStudent,
      int remainingSeats) {
    return new CourseResponse(
        courseId, code, name, credit, professorName, courseSchedule, maxStudent, remainingSeats);
  }
}

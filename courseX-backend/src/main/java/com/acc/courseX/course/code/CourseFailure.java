package com.acc.courseX.course.code;

import static lombok.AccessLevel.PRIVATE;

import com.acc.courseX.common.code.FailureCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum CourseFailure implements FailureCode {
  // 400
  COURSE_FULL(HttpStatus.BAD_REQUEST, "여석이 존재하지 않습니다"),

  // 404
  NOT_FOUND_COURSE(HttpStatus.NOT_FOUND, "해당하는 강의를 찾을 수 없습니다");

  private final HttpStatus status;
  private final String message;
}

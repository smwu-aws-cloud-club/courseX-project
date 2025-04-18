package com.acc.courseX.course.code;

import static lombok.AccessLevel.PRIVATE;

import com.acc.courseX.common.code.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum CourseSuccess implements SuccessCode {
  // 200
  GET_COURSE_LIST(HttpStatus.OK, "강의 목록 조회에 성공했습니다."),

  // 201
  COURSE_ENROLLMENT_SUCCESS(HttpStatus.CREATED, "수강신청에 성공했습니다");

  private final HttpStatus status;
  private final String message;
}

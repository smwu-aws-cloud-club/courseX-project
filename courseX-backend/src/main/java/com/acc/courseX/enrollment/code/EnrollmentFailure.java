package com.acc.courseX.enrollment.code;

import static lombok.AccessLevel.PRIVATE;

import com.acc.courseX.common.code.FailureCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum EnrollmentFailure implements FailureCode {
  // 400
  MAJOR_MISMATCH(HttpStatus.BAD_REQUEST, "전공이 일치하는 전공수업이 아닙니다"),
  ALREADY_ENROLLED(HttpStatus.BAD_REQUEST, "이미 수강 신청이 완료된 강의입니다"),
  TIMETABLE_CONFLICT(HttpStatus.BAD_REQUEST, "시간표가 중복되는 강의가 존재합니다");

  private final HttpStatus status;
  private final String message;
}

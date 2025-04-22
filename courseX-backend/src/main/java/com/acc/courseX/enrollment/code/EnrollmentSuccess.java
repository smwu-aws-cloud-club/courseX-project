package com.acc.courseX.enrollment.code;

import static lombok.AccessLevel.PRIVATE;

import com.acc.courseX.common.code.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum EnrollmentSuccess implements SuccessCode {
  // 200
  GET_ENROLLMENT_LIST(HttpStatus.OK, "수강 신청 목록 조회에 성공했습니다");

  private final HttpStatus status;
  private final String message;
}

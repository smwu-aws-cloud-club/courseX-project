package com.acc.courseX.common.code;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AuthFailure implements FailureCode {
  // 400
  INVALID_AUTH_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 id입니다");

  private final HttpStatus status;
  private final String message;
}

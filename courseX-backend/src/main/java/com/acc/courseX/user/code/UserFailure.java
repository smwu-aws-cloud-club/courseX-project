package com.acc.courseX.user.code;

import static lombok.AccessLevel.PRIVATE;

import com.acc.courseX.common.code.FailureCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum UserFailure implements FailureCode {
  // 404
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다");

  private final HttpStatus status;
  private final String message;
}

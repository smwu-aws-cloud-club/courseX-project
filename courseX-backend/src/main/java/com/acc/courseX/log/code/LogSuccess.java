package com.acc.courseX.log.code;

import com.acc.courseX.common.code.SuccessCode;

import org.springframework.http.HttpStatus;

public enum LogSuccess implements SuccessCode {
  GET_LOG_LIST(HttpStatus.OK, "로그 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  LogSuccess(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getStatus() {
    return httpStatus;
  }
}

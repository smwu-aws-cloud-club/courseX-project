package com.acc.courseX.common.exception;

import com.acc.courseX.common.response.BaseResponse;
import com.acc.courseX.common.response.ResponseUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
  @ExceptionHandler(BaseException.class)
  ResponseEntity<BaseResponse<?>> handleBusinessException(final BaseException e) {
    log.error(e.getError().getMessage());
    return ResponseUtil.failure(e.getError(), BaseResponse.ofFailure(e.getError()));
  }
}

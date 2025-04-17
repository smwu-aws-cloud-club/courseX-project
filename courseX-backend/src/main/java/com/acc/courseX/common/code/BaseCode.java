package com.acc.courseX.common.code;

import org.springframework.http.HttpStatus;

public interface BaseCode {
  HttpStatus getStatus();

  String getMessage();
}

package com.acc.courseX.support.code;

import org.springframework.http.HttpStatus;

public interface BaseCode {
  HttpStatus getStatus();

  String getMessage();
}

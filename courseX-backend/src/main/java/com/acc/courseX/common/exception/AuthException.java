package com.acc.courseX.common.exception;

import com.acc.courseX.common.code.FailureCode;

public class AuthException extends BaseException {
  public AuthException(FailureCode failure) {
    super(failure);
  }
}

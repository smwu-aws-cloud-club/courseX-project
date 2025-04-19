package com.acc.courseX.user.exception;

import com.acc.courseX.common.code.FailureCode;
import com.acc.courseX.common.exception.BaseException;

public class UserException extends BaseException {
  public UserException(FailureCode failure) {
    super(failure);
  }
}

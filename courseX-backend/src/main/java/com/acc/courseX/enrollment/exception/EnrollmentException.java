package com.acc.courseX.enrollment.exception;

import com.acc.courseX.common.code.FailureCode;
import com.acc.courseX.common.exception.BaseException;

public class EnrollmentException extends BaseException {
  public EnrollmentException(FailureCode failure) {
    super(failure);
  }
}

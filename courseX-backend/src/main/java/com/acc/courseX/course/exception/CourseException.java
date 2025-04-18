package com.acc.courseX.course.exception;

import com.acc.courseX.common.code.FailureCode;
import com.acc.courseX.common.exception.BaseException;

public class CourseException extends BaseException {
  public CourseException(FailureCode failure) {
    super(failure);
  }
}

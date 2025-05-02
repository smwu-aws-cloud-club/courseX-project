package com.acc.courseX.log.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogAction {
  VIEW_COURSE("view_course"),
  ENROLL_COURSE("enroll_course"),
  DROP_COURSE("drop_course"),
  ENROLL_COURSE_FAILURE("enroll_course_failure"),
  DROP_COURSE_FAILURE("drop_course_failure");

  private final String actionName;
}

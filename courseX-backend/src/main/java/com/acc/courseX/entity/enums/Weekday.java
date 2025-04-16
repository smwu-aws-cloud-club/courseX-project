package com.acc.courseX.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Weekday {
  MON("월"),
  TUE("화"),
  WED("수"),
  THU("목"),
  FRI("금");

  private final String displayName;
}

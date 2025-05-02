package com.acc.courseX.log.service;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.acc.courseX.log.dto.LogResponse;

public interface LogService {
  void createLog(
      Long userId,
      String actionType,
      String targetTable,
      Long targetId,
      String metadata,
      HttpServletRequest request);

  List<LogResponse> getLogs(
      Long userId,
      String actionType,
      String targetTable,
      LocalDateTime startDate,
      LocalDateTime endDate);
}

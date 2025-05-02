package com.acc.courseX.log.controller;

import static com.acc.courseX.log.code.LogSuccess.GET_LOG_LIST;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.acc.courseX.common.response.ResponseUtil;
import com.acc.courseX.log.dto.LogResponse;
import com.acc.courseX.log.entity.LogAction;
import com.acc.courseX.log.service.LogService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {
  private final LogService logService;

  @GetMapping
  public ResponseEntity<?> getLogs(
      @RequestParam(required = false) Long userId,
      @RequestParam(required = false) LogAction actionType,
      @RequestParam(required = false) String targetTable,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate) {

    LocalDateTime startDateTime =
        startDate != null ? LocalDateTime.of(startDate, LocalTime.MIN) : null;
    LocalDateTime endDateTime = endDate != null ? LocalDateTime.of(endDate, LocalTime.MAX) : null;

    String actionTypeName = (actionType != null) ? actionType.getActionName() : null;

    List<LogResponse> response =
        logService.getLogs(userId, actionTypeName, targetTable, startDateTime, endDateTime);
    return ResponseUtil.success(GET_LOG_LIST, response);
  }
}

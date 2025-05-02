package com.acc.courseX.log.dto;

import java.time.LocalDateTime;

import com.acc.courseX.log.entity.Log;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogResponse {
  private Long id;
  private Long userId;
  private String userName;
  private String actionType;
  private String targetTable;
  private Long targetId;
  private String metadata;
  private String ipAddress;
  private String userAgent;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  public static LogResponse from(Log log) {
    return LogResponse.builder()
        .id(log.getId())
        .userId(log.getUser() != null ? log.getUser().getId() : null)
        .userName(log.getUser() != null ? log.getUser().getName() : null)
        .actionType(log.getActionType())
        .targetTable(log.getTargetTable())
        .targetId(log.getTargetId())
        .metadata(log.getMetadata())
        .ipAddress(log.getIpAddress())
        .userAgent(log.getUserAgent())
        .createdAt(log.getCreatedAt())
        .build();
  }
}

package com.acc.courseX.log.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import com.acc.courseX.log.dto.LogResponse;
import com.acc.courseX.log.entity.Log;
import com.acc.courseX.log.repository.LogRepository;
import com.acc.courseX.user.entity.User;
import com.acc.courseX.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
  private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
  private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
  private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
  private static final String UNKNOWN = "unknown";

  private final LogRepository logRepository;
  private final UserRepository userRepository;

  @Async("logTaskExecutor")
  @Override
  @Transactional
  public void createLog(
      Long userId,
      String actionType,
      String targetTable,
      Long targetId,
      String metadata,
      HttpServletRequest request) {
    User user = null;
    if (userId != null) {
      user = userRepository.findById(userId).orElse(null);
    }

    String ipAddress = getClientIp(request);
    String userAgent = request.getHeader("User-Agent");

    Log log =
        Log.builder()
            .user(user)
            .actionType(actionType)
            .targetTable(targetTable)
            .targetId(targetId)
            .metadata(metadata)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .build();

    logRepository.save(log);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LogResponse> getLogs(
      Long userId,
      String actionType,
      String targetTable,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    List<Log> logs =
        logRepository.findWithFilters(userId, actionType, targetTable, startDate, endDate);
    return logs.stream().map(LogResponse::from).collect(Collectors.toList());
  }

  private String getClientIp(HttpServletRequest request) {
    String ipAddress = request.getHeader(HEADER_X_FORWARDED_FOR);
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader(HEADER_PROXY_CLIENT_IP);
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader(HEADER_WL_PROXY_CLIENT_IP);
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
    }
    return ipAddress;
  }
}

package com.acc.courseX.common.response;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import com.acc.courseX.common.code.FailureCode;
import com.acc.courseX.common.code.SuccessCode;
import com.acc.courseX.common.exception.BaseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@UtilityClass
public final class ResponseUtil {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final String UTF_8 = "UTF-8";

  public static void generateErrorResponse(
      final HttpServletResponse response, final BaseException exception) throws IOException {
    String bodyValue = MAPPER.writeValueAsString(BaseResponse.ofFailure(exception.getError()));

    response.setStatus(exception.getError().getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.getWriter().write(bodyValue);
  }

  public static <T> ResponseEntity<BaseResponse<?>> success(SuccessCode code, T data) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofSuccess(code, data));
  }

  public static ResponseEntity<BaseResponse<?>> success(SuccessCode code) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofSuccess(code));
  }

  public static <T> ResponseEntity<BaseResponse<?>> success(
      SuccessCode code, HttpHeaders headers, T data) {
    return ResponseEntity.status(code.getStatus())
        .headers(headers)
        .body(BaseResponse.ofSuccess(code, data));
  }

  public static <T> ResponseEntity<BaseResponse<?>> failure(FailureCode code, T data) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofFailure(code, data));
  }

  public static ResponseEntity<BaseResponse<?>> failure(FailureCode code) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofFailure(code));
  }
}

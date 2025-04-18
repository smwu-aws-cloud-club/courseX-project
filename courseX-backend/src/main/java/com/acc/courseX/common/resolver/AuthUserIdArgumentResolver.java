package com.acc.courseX.common.resolver;

import static com.acc.courseX.common.code.AuthFailure.INVALID_AUTH_ID;

import com.acc.courseX.common.annotation.AuthUserId;
import com.acc.courseX.common.exception.AuthException;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserIdArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String AUTH_HEADER_NAME = "X-USER-ID";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasAuthUserIdAnnotation = parameter.hasParameterAnnotation(AuthUserId.class);
    boolean isUserIdLong = parameter.getParameterType().equals(Long.class);
    return hasAuthUserIdAnnotation && isUserIdLong;
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    String header = webRequest.getHeader(AUTH_HEADER_NAME);
    boolean hasNotAuthHeader = header == null;

    if (hasNotAuthHeader) {
      throw new AuthException(INVALID_AUTH_ID);
    }

    try {
      return Long.parseLong(header);
    } catch (NumberFormatException e) {
      throw new AuthException(INVALID_AUTH_ID);
    }
  }
}

package com.jungwoo.apiserver.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
/**
 * fileName     : JwtAuthenticationEntryPoint
 * author       : jungwoo
 * description  : 인증에 실패한 사용자의 response에 HttpServletResponse.SC_UNAUTHORIZED를 담아줌, 401 에러
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = -7858869558953243875L;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}
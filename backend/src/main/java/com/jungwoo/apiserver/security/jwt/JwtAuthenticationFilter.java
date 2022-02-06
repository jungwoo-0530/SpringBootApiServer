package com.jungwoo.apiserver.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
인증에 성공하면 authentication객체를 Context안에 넣는다.

인증에 실패하면 아무런 과정 없이 다음 필터로 넘어간다.

 */


public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  public JwtAuthenticationFilter(JwtAuthenticationProvider provider) {
    jwtAuthenticationProvider = provider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = jwtAuthenticationProvider.resolveToken(request);

    if(token != null && jwtAuthenticationProvider.validateToken(token)){
      Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
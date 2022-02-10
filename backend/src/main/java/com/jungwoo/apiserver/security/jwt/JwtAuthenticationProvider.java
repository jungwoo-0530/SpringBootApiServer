package com.jungwoo.apiserver.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * fileName     : JwtAuthenticationProvider
 * author       : jungwoo
 * description  : JWT을 생성, 검증, 정보추출 해주는 클래스이다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

  private final UserDetailsService userDetailsService;

  @Value(("${security.jwt.token.secretKey}"))
  private String secretKey;
  @Value("${security.jwt.token.expire}")
  private Long tokenExpired;

  private static final String AUTHORIZATION = "Authorization";
  private static final String ACCESS_TOKEN_TYPE =
       JwtAuthenticationProvider.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";



  // JWT 토큰 생성
  public String createToken(String userPk) {
    Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
//    claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims) // 정보 저장
        .setIssuedAt(now) // 토큰 발행 시간 정보
        .setExpiration(new Date(now.getTime() + tokenExpired)) // set Expire Time
        .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
        // signature 에 들어갈 secret값 세팅
        .compact();
  }

  public String getRole(String jwt) {
    Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
    return (String) claims.getBody().get("roles");
  }

  // JWT 토큰에서 인증 정보 조회
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 토큰의 만료기간 조회 분으로 환산 후.
//  public Long getTokenExpired(){
////    return tokenExpired/60000;
//    return tokenExpired;
//  }
  // 토큰의 만료기간 조회 분으로 환산 후.
  public Date getTokenExpired(String token){
//    return tokenExpired/60000;
    Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

    return claims.getBody().getExpiration();
  }

  // 토큰에서 회원 정보 추출
  public String getUserPk(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
  public String resolveToken(HttpServletRequest request, String type) {
//    String token = null;
//    Cookie cookie = WebUtils.getCookie(request, "x-access-token");
//    if (cookie != null) token = cookie.getValue();
//    return token;
    Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
    while(headers.hasMoreElements()){
      String value = headers.nextElement();
      if(value.toLowerCase().startsWith(type.toLowerCase())) {
        return value.substring(type.length()).trim();
      }
    }

    return Strings.EMPTY;
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
      log.info(String.valueOf(claims.getBody().getExpiration()));
      log.info(String.valueOf(new Date()));
      return !claims.getBody().getExpiration().before(new Date());
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }


}

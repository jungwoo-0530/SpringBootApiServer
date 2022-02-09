package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.dto.member.CreateMemberRequest;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * fileName     : MemberController
 * author       : jungwoo
 * description  : Member Entity와 관련된 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public dupCheck registerMember(@RequestBody CreateMemberRequest createMemberRequest)
  {
    dupCheck result = new dupCheck("",
        memberService.dupLoginIdCheck(createMemberRequest.getLoginId()),
        memberService.dupEmailCheck(createMemberRequest.getEmail()));

    if(result.dupLoginIdCheck){
      result.setMessage("이미 사용 중인 아이디입니다.");
    }
    else if(result.dupEmailCheck) {
      result.setMessage("이미 사용 중인 이메일입니다.");
    }
    else{

      Member newMember = Member.builder().
          name(createMemberRequest.getName()).
          loginId(createMemberRequest.getLoginId()).
          email(createMemberRequest.getEmail()).
          password(createMemberRequest.getPassword()).
          telephone(createMemberRequest.getTelephone()).
          role("MEMBER").build();

      result.setMessage("회원가입이 완료되었습니다.");
      memberService.save(newMember);
    }

    return result;
  }

  @Data
  @AllArgsConstructor
  public static class dupCheck{

    String message;
    boolean dupLoginIdCheck;
    boolean dupEmailCheck;

  }


@PostMapping("/login")
public ResponseEntity<TokenResponse> login(@RequestBody loginRequest loginReq){
  Member member = memberService.findByLoginId(loginReq.getLoginId())
      .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID입니다."));
  if (!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
    throw new IllegalArgumentException("잘못된 비밀번호입니다.");
  }

  String token = jwtAuthenticationProvider.createToken(member.getLoginId());
  return ResponseEntity.ok().body(TokenResponse.builder()
      .tokenType("bearer")
      .accessToken(token)
      .tokenExpired(jwtAuthenticationProvider.getTokenExpired())
      .build());
}
  @Getter
  @Builder
  public static class TokenResponse {
    private String accessToken;
    private Long tokenExpired;
    private String tokenType;
  }

  @Getter
  @Builder
  public static class loginRequest{

    private String loginId;
    private String password;
  }

//  request 헤더에 있는 JWT 토큰값으로 해당하는 사용자.
//  즉, 현재 로그인한 사용자의 정보(loginId, role)을 가져올 수 있음.
//  ResponseEntity<>로 응답하기.
  @GetMapping("/auth")
  public Map<String, String> memberAuth(HttpServletRequest req){

//    Cookie[] cookies = req.getCookies();
//    for(Cookie a : cookies){
//      System.out.println(a.getName() + " :" +a.getValue());
//    }
//    String authorization = req.getHeader("Co");
//    String token = jwtAuthenticationProvider.resolveToken(req);
//    String loginId = jwtAuthenticationProvider.getUserPk(token);//loginId 출력.
//    String role = jwtAuthenticationProvider.getRole(token);
//
//    return AuthResponse.builder()
//        .auth(role).build();
//    return AuthResponse.builder()
//        .auth(authorization).build();
    String a = jwtAuthenticationProvider.getRole(jwtAuthenticationProvider.resolveToken(req,"Bearer"));
    Map<String, String> map = new HashMap<>();
    map.put("role", a);
    return map;
  }


  @Getter
  @Builder
  public static class AuthResponse{

    private String auth;

  }



  @PostMapping("/test/auth")
  private String getToken(HttpServletRequest request) {
    log.info("testAuth");

    String header = request.getHeader("Authorization");

    log.info(header);

    if (header != null && header.startsWith("Bearer ")) {
      return header.replace("Bearer ","");
    }

    return null;
  }


}

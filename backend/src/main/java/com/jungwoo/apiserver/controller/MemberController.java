package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.Dto.Member.CreateMemberRequest;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * fileName     : MemberController
 * author       : jungwoo
 * description  : Member Entity와 관련된 컨트롤러
 */
@RestController
@RequiredArgsConstructor
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
  public LoginForm login(@RequestBody LoginForm loginForm,
                    HttpServletResponse response){
    Member member = memberService.findByLoginId(loginForm.getLoginId())
        .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID입니다."));
    if (!passwordEncoder.matches(loginForm.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("잘못된 비밀번호입니다.");
    }

    String token = jwtAuthenticationProvider.createToken(member.getLoginId(), member.getRole());
    response.setHeader("x-access-token", token);

    Cookie cookie = new Cookie("x-access-token", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    response.addCookie(cookie);

    return LoginForm.builder()
        .loginId(member.getLoginId())
        .role(member.getRole())
        .build();
  }

  @Getter
  @Builder
  public static class LoginForm{

    private String loginId;
    private String password;
    private String role;
  }

  //request 헤더에 있는 JWT 토큰값으로 해당하는 사용자.
  //즉, 현재 로그인한 사용자의 정보(loginId, role)을 가져올 수 있음.
  @GetMapping("/auth")
  public AuthResponse memberAuth(HttpServletRequest req){

    String token = jwtAuthenticationProvider.resolveToken(req);
    String loginId = jwtAuthenticationProvider.getUserPk(token);//loginId 출력.
    String role = jwtAuthenticationProvider.getRole(token);

    return AuthResponse.builder()
        .loginId(loginId)
        .auth(role).build();
  }


  @Getter
  @Builder
  public static class AuthResponse{

    private String loginId;
    private String auth;

  }







}

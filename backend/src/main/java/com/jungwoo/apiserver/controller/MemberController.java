package com.jungwoo.apiserver.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.dto.ErrorResponse;
import com.jungwoo.apiserver.dto.member.CreateMemberRequest;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * fileName     : MemberController
 * author       : jungwoo
 * description  : Member Entity와 관련된 컨트롤러
 */
@Api(tags = "회원 API 정보를 제공하는 Controller")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final PasswordEncoder passwordEncoder;

  @ApiOperation(value = "회원가입하는 메소드")
  @PostMapping("/register")
  public ResponseEntity<? extends BasicResponse> registerMember(@RequestBody CreateMemberRequest createMemberRequest) {
    if (memberService.dupLoginIdCheck(createMemberRequest.getLoginId())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("이미 사용 중인 아이디입니다."));
    } else if (memberService.dupEmailCheck(createMemberRequest.getEmail())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("이미 사용 중인 이메일입니다."));
    } else {

      Member newMember = Member.builder().
          name(createMemberRequest.getName()).
          loginId(createMemberRequest.getLoginId()).
          email(createMemberRequest.getEmail()).
          password(createMemberRequest.getPassword()).
          telephone(createMemberRequest.getTelephone()).
          role("MEMBER").build();

      Long id = memberService.save(newMember);


      return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(id, "회원가입이 완료되었습니다."));
    }
  }

  @Data
  @AllArgsConstructor
  public static class registerDto {
    Long id;
    String loginId;
  }

  @ApiOperation(value = "로그인하는 메소드")
  @PostMapping("/login")
  public ResponseEntity<? extends BasicResponse> login(@RequestBody loginRequest loginReq) {

    Optional<Member> optionalMember = memberService.findByLoginId(loginReq.getLoginId());
    if (!optionalMember.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).
          body(new ErrorResponse("가입되지 않은 LoginId입니다."));
    }
    Member member = optionalMember.get();
    if (!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
          body(new ErrorResponse("비밀번호가 일치하지 않습니다."));
    }

    String token = jwtAuthenticationProvider.createToken(member.getLoginId());
    Date date = jwtAuthenticationProvider.getTokenExpired(token);
    return ResponseEntity.ok().body(new CommonResponse<>(TokenResponse.builder().
        accessToken(token).
        tokenExpired(date).
        tokenType("Bearer").build()));
  }

  @Getter
  @Builder
  public static class TokenResponse {
    private String accessToken;
    private Date tokenExpired;
    private String tokenType;
  }

  @Getter
  @Builder
  public static class loginRequest {

    private String loginId;
    private String password;
  }

  //  request 헤더에 있는 JWT 토큰값으로 해당하는 사용자.
//  즉, 현재 로그인한 사용자의 정보(loginId, role)을 가져올 수 있음.
//  ResponseEntity<>로 응답하기.
  @GetMapping("/auth")
  public ResponseEntity<? extends BasicResponse> getRoleAndLoingId(HttpServletRequest req) {
    String token = jwtAuthenticationProvider.resolveToken(req, "Bearer");
    String role = jwtAuthenticationProvider.getRole(token);
    String loginId = jwtAuthenticationProvider.getUserPk(token);
    return ResponseEntity.ok().body(new CommonResponse<>(AuthResponse.builder().
        role(role).
        loginId(loginId).build()));
  }


  @Getter
  @Builder
  public static class AuthResponse {
    private String loginId;
    private String role;
  }

  @GetMapping("/members/me")
  public ResponseEntity<? extends BasicResponse> memberDetail(HttpServletRequest request) {
    log.info("memberDetail in MemberController");

    String token = jwtAuthenticationProvider.resolveToken(request, "Bearer");
    Member member = memberService.findByJwt(token);


    return ResponseEntity.ok().body(new CommonResponse<>(MemberDto.builder().loginId(member.getLoginId()).
        email(member.getEmail()).
        name(member.getName()).
        telephone(member.getTelephone()).
        role(member.getRole()).build(), "정상적으로 회원을 찾았습니다."));
  }


  @Getter
  @Builder
  public static class MemberDto {

    private String name;
    private String loginId;
    private String email;
    private String telephone;
    private String role;
  }

  @PutMapping("/members/me/update")
  public ResponseEntity<? extends BasicResponse> updateMe(@RequestBody MemberDto memberDto) {
    Member member = Member.builder().name(memberDto.getName()).
        loginId(memberDto.getLoginId()).
        email(memberDto.getEmail()).
        role(memberDto.getRole()).
        telephone(memberDto.getTelephone()).build();

    memberService.updateMember(member);

    return ResponseEntity.ok().body(new CommonResponse<>("회원 업데이트를 성공했습니다."));
  }


  @PostMapping("/logout")
  public ResponseEntity<? extends BasicResponse> logout() {

    return ResponseEntity.ok().body(new CommonResponse<>("로그아웃에 성공했습니다."));
  }
}

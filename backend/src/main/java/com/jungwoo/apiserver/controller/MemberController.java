package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.Dto.Member.CreateMemberRequest;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.serviece.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/api/users/new")
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







}

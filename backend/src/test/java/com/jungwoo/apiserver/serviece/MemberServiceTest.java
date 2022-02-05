package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

  @Mock
  MemberRepository memberRepository;

  @Mock
  MemberService memberService;

  @Test
  @DisplayName("멤버아이디중복체크")
  public void 멤버아이디중복체크() throws Exception{
      //given
    Member newMember = Member.builder().
        loginId("am7227").
        password("1234").
        name("김정우").
        email("am7227@naver.com").
        telephone("010-8541-9497").build();
    memberRepository.save(newMember);
      //when
    String loginId = "am7227";
      //then

    assertThat(memberService.dupLoginIdCheck(loginId)).isTrue();

  }

}
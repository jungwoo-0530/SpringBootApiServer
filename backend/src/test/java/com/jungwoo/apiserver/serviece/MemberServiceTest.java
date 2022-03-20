package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Mock
  MemberRepository memberRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  JwtAuthenticationProvider jwtAuthenticationProvider;

  @InjectMocks
  MemberService memberService;

  @Test
  @DisplayName("멤버아이디중복체크")
  void 멤버아이디중복체크() throws Exception{
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


  @Test
  @DisplayName("Auditing적용")
  public void Auditing적용() throws Exception{
      //given
    ZonedDateTime before = ZonedDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"));

      Member member = Member.builder().
          loginId("am7227").
          password("1234").
          name("김정우").
          email("am7227@naver.com").
          telephone("010-8541-9497").build();
      memberRepository.save(member);

    //when
    List<Member> list = memberRepository.findAll();
    Member findMember = list.get(0);



      //then
    assertThat(findMember.getCreateDate()).isAfter(before);

  }

  @Test
  @DisplayName("멤버 저장")
  void 멤버_저장() throws Exception{
    //given
    Member member = new Member(0L, "am7227", "password", "김정우", "am7227@naver.com", "010-0000-0000", "admin");

    //when
    when(memberService.save(member)).thenReturn(0L);

    //then
    assertThat(memberService.save(member)).isEqualTo(0L);

  }

  @Test
  @DisplayName("토큰으로 Member Get")
  void 토큰으로_Member_Get() throws Exception{
    //given
    String token = jwtAuthenticationProvider.createToken("am7227");
    Member member = new Member(0L, "am7227", "password", "김정우", "am7227@naver.com", "010-0000-0000", "admin");

    //when
//    when(memberService.getMemberByJwt(token)).thenReturn(member);
    doReturn(member).when(memberService).getMemberByJwt(token);

    //then
    assertThat(memberService.getMemberByJwt(token)).isEqualTo(member);

  }

}
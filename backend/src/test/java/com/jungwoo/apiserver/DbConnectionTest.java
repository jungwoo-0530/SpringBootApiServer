package com.jungwoo.apiserver;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class DbConnectionTest {

  @Mock
  MemberRepository memberRepository;


//  @Test
//  @DisplayName("MariaDB 연결 확인")
//  public void MariaDB연결확인(){
//
//    Member newMember = new Member("jungwoo");
//
//    memberRepository.save(newMember);
//
//
//    List<Member> memberList = memberRepository.findAll();
//
//    Member member = memberList.get(0);
//    assertThat(member.getName()).isEqualTo("jungwoo");
//
//  }
}


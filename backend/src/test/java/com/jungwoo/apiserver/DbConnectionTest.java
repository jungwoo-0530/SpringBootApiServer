package com.jungwoo.apiserver;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class DbConnectionTest {

  @Autowired
  MemberRepository memberRepository;


  @Test
  public void TableCreate(){

    Member newMember = new Member("jungwoo");

    memberRepository.save(newMember);


    List<Member> memberList = memberRepository.findAll();

    Member member = memberList.get(0);
    assertThat(member.getName()).isEqualTo("jungwoo");

  }
}

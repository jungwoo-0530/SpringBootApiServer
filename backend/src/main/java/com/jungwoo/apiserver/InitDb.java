package com.jungwoo.apiserver;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.domain.Post;
import com.jungwoo.apiserver.repository.MemberRepository;
import com.jungwoo.apiserver.repository.PostRepository;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * fileName     : InitDb
 * author       : jungwoo
 * description  :
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InitDb {

  private final PostRepository postRepository;
  private final MemberRepository memberRepository;
  private final PostService postService;
  private final MemberService memberService;

  @PostConstruct
  @Transactional
  public void init(){

    Member member = Member.builder().loginId("am7227").password("asdf1234!!").email("am7227@naver.com")
        .role("admin").name("김정우").telephone("010-8541-9497").build();
    memberService.save(member);

    for(int i = 0; i<20; i++)
    {
      Post post = Post.builder().
          title("제묵"+i).content("테스트입니다"+ i).type("qna").hit(1L).member(memberRepository.getById(0L)).build();
      postService.saveWithMember(post,member);
    }

    log.info("DB INIT PASS");
  }


}

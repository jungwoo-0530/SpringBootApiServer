package com.jungwoo.apiserver;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Comment;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import com.jungwoo.apiserver.repository.BoardRepository;
import com.jungwoo.apiserver.serviece.CommentService;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.BoardService;
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

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;
  private final BoardService boardService;
  private final MemberService memberService;
  private final CommentService commentService;
//
//  @PostConstruct
//  @Transactional
//  public void init(){
//
//    Member admin = Member.builder().loginId("aa").password("aa").email("admin@naver.com")
//        .role("admin").name("김정우").telephone("010-8541-9497").build();
//    memberService.save(admin);
//
//    Member member = Member.builder().loginId("bb").password("bb").email("member@naver.com")
//        .role("member").name("김선우").telephone("010-8541-9497").build();
//
//    memberService.save(member);
//
//    for(int i = 0; i<20; i++)
//    {
//      Board board = Board.builder().
//          title("제묵"+i).content("테스트입니다"+ i).type("qna").hit(1).member(memberRepository.getById(0L)).available(true).build();
//      boardService.saveWithMember(board,admin);
//    }
//
//    Board commentBoard = Board.builder().
//        title("CommentTest").content("ComentTest").type("qna").hit(1).member(memberRepository.getById(0L)).available(true).build();
//    boardService.saveWithMember(commentBoard, admin);
//
//    for(int j = 0; j<11; j++){
//      Comment comment = Comment.builder().
//        content("댓글" + j).available(true).member(admin).board(commentBoard).build();
//
//      commentService.createComment(comment);
//    }
//
//    log.info("DB INIT PASS");
//  }


}

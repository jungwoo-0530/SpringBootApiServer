package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Comment;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * fileName     : CommentServiceTest
 * author       : jungwoo
 * description  :
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  CommentRepository commentRepository;

  @InjectMocks
  CommentService commentService;


  @Test
  @DisplayName("댓글_저장")
  public void 댓글_저장() throws Exception{
      //given
    Member member = new Member(0L, "am7227", "password", "김정우", "am7227@naver.com", "010-8541-9497", "admin");
    Board board = new Board(0L, "테스트1", "테스트1", "qna", 0, true, member);
//    Comment comment = new Comment(0L, "댓글1", true, board, member);

      //when

//    Long commentId = commentService.createComment(comment);

    //then
//
//    assertThat(commentId).isEqualTo(0L);
//    System.out.println("commentId = " + commentId);

  }





}
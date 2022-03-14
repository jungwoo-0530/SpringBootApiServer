package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Comment;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.dto.ErrorResponse;
import com.jungwoo.apiserver.dto.comment.CommentPageDto;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.BoardService;
import com.jungwoo.apiserver.serviece.CommentService;
import com.jungwoo.apiserver.serviece.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * fileName     : CommentController
 * author       : jungwoo
 * description  :
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final MemberService memberService;
  private final BoardService boardService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;


  @ApiOperation(value = "게시글 하나에 존재하는 댓글 리스트")
  @GetMapping("/test/comments")
  public Page<CommentPageDto> listComment(@RequestParam(value = "boardId") Long boardId,
                                          @PageableDefault(size = 5, sort = "id",
                                              direction = Sort.Direction.ASC) Pageable pageable) {

    return commentService.findPageSort(boardId, pageable);

  }

  @PostMapping("/test/commnets")
  public ResponseEntity<? extends BasicResponse> createComment(@PathVariable(name = "boardId") Long boardId,
                                                               @RequestBody CommentDto commentDto,
                                                               HttpServletRequest request) {

    Member member = memberService.getMemberByJwt(jwtAuthenticationProvider.getTokenInRequestHeader(request, "Bearer"));
    Board board = boardService.getBoardById(boardId);
    Long commentId = commentService.createComment(Comment.builder().
        content(commentDto.getContent()).
        available(true).
        member(member).
        board(board).build());

    if (commentId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("댓글 생성 실패"));
    }else{
      return ResponseEntity.status(201).body(new CommonResponse<>(commentId, "게시물을 생성했습니다."));
    }
  }

  @Getter
  @Builder
  private static class CommentDto{
    Long id;
    String content;
  }
}

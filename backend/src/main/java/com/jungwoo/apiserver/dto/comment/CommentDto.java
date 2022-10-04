package com.jungwoo.apiserver.dto.comment;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Comment;
import com.jungwoo.apiserver.domain.Member;
import lombok.Builder;
import lombok.Data;

/**
 * fileName     : CommentDto
 * author       : jungwoo
 * description  :
 */
@Builder
@Data
public class CommentDto {

  private Long id;
  private String content;
  private Long boardId;
  private Long parentId;

  private Board board;
  private Member member;

  private Comment parentComment;
  private Comment rootComment;

}

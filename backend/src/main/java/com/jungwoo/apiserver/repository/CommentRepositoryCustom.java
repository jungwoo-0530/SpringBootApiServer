package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Comment;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.dto.comment.CommentPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * fileName     : CommentRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface CommentRepositoryCustom {


  public Page<CommentPageDto> findCommentsOrderByHierarchy(Pageable pageable, Long boardId, Member member);

  public void adjustHierarchyOrders(Comment newComment);

}

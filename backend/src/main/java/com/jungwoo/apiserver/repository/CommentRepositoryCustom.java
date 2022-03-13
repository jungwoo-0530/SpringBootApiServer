package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.dto.comment.CommentPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * fileName     : CommentRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface CommentRepositoryCustom {
  Page<CommentPageDto> findAllPageSort(Long boardId, Pageable pageable);
}

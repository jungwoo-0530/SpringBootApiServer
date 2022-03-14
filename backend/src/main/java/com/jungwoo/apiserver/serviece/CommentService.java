package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Comment;
import com.jungwoo.apiserver.dto.comment.CommentPageDto;
import com.jungwoo.apiserver.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * fileName     : CommentService
 * author       : jungwoo
 * description  :
 */
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;


  @Transactional(readOnly = true)
  public Page<CommentPageDto> findPageSort(Long boardId, Pageable pageable) {
    return commentRepository.findAllPageSort(boardId, pageable);
  }

  @Transactional
  public Long createComment(Comment comment) {

    commentRepository.save(comment);

    return comment.getId();

  }
}

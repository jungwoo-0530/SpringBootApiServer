package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.dto.board.BoardPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * fileName     : BoardRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface BoardRepositoryCustom {
  Page<BoardPageDto> findAllPageSort(String boardType, Pageable pageable);

}

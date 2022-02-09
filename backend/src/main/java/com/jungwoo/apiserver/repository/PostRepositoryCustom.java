package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.dto.post.PostPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * fileName     : BoardRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface PostRepositoryCustom {
  Page<PostPageDto> findAllPageSort(String postType, Pageable pageable);

}

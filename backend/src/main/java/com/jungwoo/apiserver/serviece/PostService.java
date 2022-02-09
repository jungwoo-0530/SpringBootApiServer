package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.domain.Post;
import com.jungwoo.apiserver.dto.post.PostPageDto;
import com.jungwoo.apiserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * fileName     : PostService
 * author       : jungwoo
 * description  :
 */
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  @Transactional
  public void saveWithMember(Post post, Member member){

    post.setMember(member);
    postRepository.save(post);
  }

  @Transactional(readOnly = true)
  public Page<PostPageDto> findPageSort(String postType, Pageable pageable) {
    return postRepository.findAllPageSort(postType, pageable);
  }
}

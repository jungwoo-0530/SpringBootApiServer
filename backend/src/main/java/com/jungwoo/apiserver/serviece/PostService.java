package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.controller.PostController;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.domain.Post;
import com.jungwoo.apiserver.dto.post.PostPageDto;
import com.jungwoo.apiserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * fileName     : PostService
 * author       : jungwoo
 * description  :
 */
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  //db초기화용.
  @Transactional
  public void saveWithMember(Post post, Member member){

    post.setMember(member);
    postRepository.save(post);
  }

  @Transactional(readOnly = true)
  public Page<PostPageDto> findPageSort(String postType, Pageable pageable) {
    return postRepository.findAllPageSort(postType, pageable);
  }


  @Transactional
  public Long createPost(Post post) {
    postRepository.save(post);
    return post.getId();
  }

  @Transactional(readOnly = true)
  public Post getPostById(Long postId) {
      return postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
  }

  public Optional<Post> getPostWithMemberById(Long postId) {
    return postRepository.findByPostIdWithMember(postId);
  }

  public List<Post> getPostsAll() {
    return postRepository.findAll();
  }


  @Transactional
  public Post getPostDetail(Long postId) {

    Optional<Post> optionalPost = postRepository.findByPostIdWithMember(postId);
    if(!optionalPost.isPresent()) {
      return null;
    }

    Post post = optionalPost.get();
    post.plusViewNum(post.getHit());
    return post;
  }
}

package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.domain.Post;
import com.jungwoo.apiserver.dto.DefaultRes;
import com.jungwoo.apiserver.dto.ResponseMessage;
import com.jungwoo.apiserver.dto.post.PostPageDto;
import com.jungwoo.apiserver.dto.StatusCode;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.PostService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * fileName     : PostController
 * author       : jungwoo
 * description  :
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final MemberService memberService;


  @GetMapping("/posts/{postType}")
  public Page<PostPageDto> listPost(@PathVariable(name = "postType")String type,
                          @PageableDefault(size = 4, sort = "id",
      direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("PostController getmapping list");


    return postService.findPageSort(type, pageable);
  }

  @PostMapping("/posts/{postType}/new")
  public ResponseEntity<DefaultRes<PostRequest>> createPost(@PathVariable(name = "postType")String type,
                                   @RequestBody PostRequest postReqeust,
                                   HttpServletRequest request){

    Member member = memberService.findByJwt(jwtAuthenticationProvider.resolveToken(request, "Bearer"));
    Post post = Post.builder().
        title(postReqeust.title).
        content(postReqeust.content).
        member(member).
        type(type).
        hit(0L).build();

    postService.createPost(post);

    return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_POST,
        postReqeust), HttpStatus.OK);
  }

  @Getter
  @Builder
  private static class PostRequest{
    String title;
    String content;
    String type;
  }


}

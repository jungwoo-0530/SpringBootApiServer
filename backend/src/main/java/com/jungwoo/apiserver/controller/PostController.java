package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.domain.Post;
import com.jungwoo.apiserver.dto.DefaultRes;
import com.jungwoo.apiserver.dto.ResponseMessage;
import com.jungwoo.apiserver.dto.member.MemberDto;
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
import java.time.ZonedDateTime;
import java.util.HashMap;

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
  public Page<PostPageDto> listPost(@PathVariable(name = "postType") String type,
                                    @PageableDefault(size = 4, sort = "id",
                                        direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("PostController getmapping list");


    return postService.findPageSort(type, pageable);
  }

  @PostMapping("/posts/{postType}/new")
  public ResponseEntity<String> createPost(@PathVariable(name = "postType") String type,
                                                            @RequestBody PostDto postDto,
                                                            HttpServletRequest request) {

    Member member = memberService.findByJwt(jwtAuthenticationProvider.resolveToken(request, "Bearer"));
    Post post = Post.builder().
        title(postDto.title).
        content(postDto.content).
        member(member).
        type(type).
        hit(0L).build();

    postService.createPost(post);

    return new ResponseEntity<>(HttpStatus.OK);
  }


  //map으로 두 객체를 넣는다.
//  @GetMapping("/posts/{postId}/{postType}")
//  public ResponseEntity<DefaultRes<PostRequest>> readPost(@PathVariable(name = "postId") Long postId,
//                                                          @PathVariable(name ="postType")String postType) {
//    Post post = postService.findByIdWithMember(postId);
//    PostRequest postRequest = PostRequest.builder().
//        content(post.getContent()).
//        title(post.getTitle()).
//        author(post.getMember().getLoginId()).
//        type(postType).
//        updateTime(post.getUpdateDate()).
//        build();
//
//    return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_POST,
//        postRequest), HttpStatus.OK);
//  }

  @GetMapping("/posts/{postId}/{postType}")
  public ResponseEntity<PostDto> readPost(@PathVariable(name = "postId") Long postId,
                                                          @PathVariable(name ="postType")String postType) {
    Post post = postService.findByIdWithMember(postId);
//
//    MemberDto memberDto = MemberDto.builder().
//        name(post.getMember().getName()).
//        loginId(post.getMember().getLoginId()).
//        email(post.getMember().getEmail()).
//        role(post.getMember().getRole()).build();
//


    PostDto postDto = PostDto.builder().
        content(post.getContent()).
        title(post.getTitle()).
        author(post.getMember().getLoginId()).
        type(postType).
        email(post.getMember().getEmail()).
        updateTime(post.getUpdateDate()).
        createTime(post.getCreateDate()).
        build();

//    HashMap<String, Object> map = new HashMap<>();
//    map.put("post", postRequest);
//    map.put("member", memberDto);

    return new ResponseEntity<>(postDto, HttpStatus.OK);
  }

  @Getter
  @Builder
  private static class PostDto {
    String title;
    String content;
    String author;
    String type;
    String email;
    ZonedDateTime createTime;
    ZonedDateTime updateTime;
  }

}

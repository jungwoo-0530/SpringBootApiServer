package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.domain.Post;
import com.jungwoo.apiserver.dto.*;
import com.jungwoo.apiserver.dto.post.PostPageDto;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.util.Optional;

/**
 * fileName     : PostController
 * author       : jungwoo
 * description  :
 */
@Api(tags = "게시글 API 정보를 제공하는 Controller")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final MemberService memberService;

  @ApiOperation(value = "카테고리에 맞는 게시글 목록을 반환하는 메소드")
  @ApiImplicitParam(name = "type", value = "게시글 카테고리", dataType = "String")
  @GetMapping("/posts/{postType}")
  public Page<PostPageDto> listPost(@PathVariable(name = "postType") String type,
                                    @PageableDefault(size = 4, sort = "id",
                                        direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("PostController getmapping list");


    return postService.findPageSort(type, pageable);
  }

  @ApiOperation(value = "게시글을 생성하는 메소드")
  @ApiImplicitParam(name = "postType", value = "게시글 카테고리")
  @PostMapping("/posts/{postType}/new")
  public ResponseEntity<? extends BasicResponse> createPost(@PathVariable(name = "postType") String type,
                                           @RequestBody PostDto postDto,
                                           HttpServletRequest request) {

    Member member = memberService.findByJwt(jwtAuthenticationProvider.resolveToken(request, "Bearer"));
    Post post = Post.builder().
        title(postDto.title).
        content(postDto.content).
        member(member).
        type(type).
        hit(1L).build();

    Long id = postService.createPost(post);
    if (id == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("게시물 생성하지 못하였습니다."));
    }

    return ResponseEntity.status(201).body(new CommonResponse<>(id));
  }



  @ApiOperation(value = "게시글 하나를 읽는 메소드")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "postId", value = "게시글 아이디", paramType = "path", dataType = "String"),
      @ApiImplicitParam(name = "postType", value = "게시글 카테고리", paramType = "path", dataType = "String")
  })
  @GetMapping("/posts/{postId}/{postType}")
  public ResponseEntity<? extends BasicResponse> readPost(@PathVariable(name = "postId") Long postId,
                                          @PathVariable(name = "postType") String postType) {

    Post post = postService.findPostDetail(postId);

    if (post == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponse("해당 게시물이 존재하지 않습니다"));
    }

    PostDto postDto = PostDto.builder().
        content(post.getContent()).
        title(post.getTitle()).
        author(post.getMember().getLoginId()).
        type(postType).
        email(post.getMember().getEmail()).
        updateTime(post.getUpdateDate()).
        createTime(post.getCreateDate()).
        build();

    return ResponseEntity.ok().body(new CommonResponse<>(postDto, "게시물을 불러왔습니다."));

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

package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.dto.post.PostPageDto;
import com.jungwoo.apiserver.serviece.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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


  @GetMapping("/test/posts/{postType}")
  public Page<PostPageDto> listPost(@PathVariable(name = "postType")String type,
                          @PageableDefault(size = 4, sort = "id",
      direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("PostController getmapping list");


    return postService.findPageSort(type, pageable);
  }


}

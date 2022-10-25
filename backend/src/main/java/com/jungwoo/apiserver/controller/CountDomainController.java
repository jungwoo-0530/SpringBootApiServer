package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.serviece.DetectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

/**
 * fileName     : CountDomainController
 * author       : jungwoo
 * description  :
 */

@Controller
@RequiredArgsConstructor
public class CountDomainController {

  private final DetectService detectService;

  @GetMapping("/domain/ranking")
  public ResponseEntity<? extends BasicResponse> list(@PageableDefault(size = 4, sort = "hit",
      direction = Sort.Direction.DESC) Pageable pageable) {

    Map<String, Object> result = detectService.findCountDomainPage(pageable);

    return ResponseEntity.ok().body(new CommonResponse<>(result, "도메인 순위를 불러왔습니다."));

  }
}

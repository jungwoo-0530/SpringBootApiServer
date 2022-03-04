package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.dto.*;
import com.jungwoo.apiserver.dto.board.BoardPageDto;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Locale;

/**
 * fileName     : BoardController
 * author       : jungwoo
 * description  :
 */
@Api(tags = "게시글 API 정보를 제공하는 Controller")
@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final MemberService memberService;

  @ApiOperation(value = "카테고리에 맞는 게시글 목록을 반환하는 메소드")
  @ApiImplicitParam(name = "type", value = "게시글 카테고리", dataType = "String")
  @GetMapping("/boards")
  public Page<BoardPageDto> listBoard(@RequestParam(value = "boardType") String boardType,
                                      @PageableDefault(size = 10, sort = "id",
                                          direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("BoardController getmapping list");
    log.info("{}", memberService.getClass());


    return boardService.findPageSort(boardType, pageable);
  }

  @ApiOperation(value = "게시글 하나를 읽는 메소드")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "boardId", value = "게시글 아이디", paramType = "path", dataType = "Long"),
  })
  @GetMapping("/boards/{boardId}")
  public ResponseEntity<? extends BasicResponse> readBoard(@PathVariable(name = "boardId") Long boardId) {
    log.info("BoardController readBoard");

    Board board = boardService.getBoardDetail(boardId);

    if (!board.isAvailable()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).
          body(new ErrorResponse("해당 게시물이 존재하지 않습니다"));
    }


    BoardDto boardDto = BoardDto.builder().
        content(board.getContent()).
        title(board.getTitle()).
        author(board.getMember().getLoginId()).
        type(board.getType()).
        email(board.getMember().getEmail()).
        available(board.isAvailable()).
        updateTime(board.getUpdateDate()).
        createTime(board.getCreateDate()).
        build();


    return ResponseEntity.ok().body(new CommonResponse<>(boardDto, "게시물을 불러왔습니다."));

  }

  @Getter
  @Builder
  private static class BoardDto {
    Long id;
    String title;
    String content;
    String author;
    String type;
    String email;
    boolean available;
    ZonedDateTime createTime;
    ZonedDateTime updateTime;
  }


  @ApiOperation(value = "게시글을 생성하는 메소드")
  @ApiImplicitParam(name = "boardType", value = "게시글 카테고리")
  @PostMapping("/boards/{boardType}")
  public ResponseEntity<? extends BasicResponse> createBoard(@PathVariable(name = "boardType") String type,
                                                             @RequestBody BoardDto boardDto,
                                                             HttpServletRequest request) {

    Member member = memberService.getMemberByJwt(jwtAuthenticationProvider.getTokenInRequestHeader(request, "Bearer"));
    Board board = Board.builder().
        title(boardDto.title).
        content(boardDto.content).
        member(member).
        type(type).
        hit(1).
        available(true).
        build();

    Long id = boardService.createBoard(board);
    if (id == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("게시물 생성하지 못하였습니다."));
    }

    return ResponseEntity.status(201).body(new CommonResponse<>(id, "게시물을 생성했습니다."));
  }


  @DeleteMapping("/boards/{boardId}")
  public ResponseEntity<? extends BasicResponse> deleteBoard(@PathVariable(name = "boardId") Long boardId) {


    boardService.softDeleteBoard(boardId);

    return ResponseEntity.ok().body(new CommonResponse<>("게시물을 삭제했습니다."));

  }

  @PutMapping("/boards/{boardId}")
  public ResponseEntity<? extends BasicResponse> updateBoard(@PathVariable(name = "boardId") Long boardId,
                                                             @RequestBody BoardDto boardDto) {

    log.info("BoardController updateBoard");

    Board board = Board.builder().
        id(boardId).
        title(boardDto.getTitle()).
        content(boardDto.getContent()).build();

    boardService.updateBoard(board);

    return ResponseEntity.ok().body(new CommonResponse<>("게시물을 수정했습니다."));
  }




}

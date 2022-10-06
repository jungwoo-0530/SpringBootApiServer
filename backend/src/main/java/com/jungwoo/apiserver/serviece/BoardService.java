package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.dto.board.BoardPageDto;
import com.jungwoo.apiserver.dto.board.BoardSearchCondition;
import com.jungwoo.apiserver.repository.BoardRepository;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * fileName     : BoardService
 * author       : jungwoo
 * description  :
 */
@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  private final MemberService memberService;
  private final ImageUtilService imageUtilService;

  //db초기화용.


  @Transactional(readOnly = true)
  public Page<BoardPageDto> findPageSort(String boardType, Pageable pageable) {
    return boardRepository.findAllPageSort(boardType, pageable);
  }


  @Transactional
  public Long saveBoard(Board board) throws IOException {

    boardRepository.save(board);

    imageUtilService.permanentSaveImage(board.getMember().getLoginId(), board);


    return board.getId();
  }

  @Transactional(readOnly = true)
  public Board getBoardById(Long boardId) {
    return boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
  }



  @Transactional
  public Board getBoardAndAddHit(Long boardId) {

    Optional<Board> optionalBoard = boardRepository.findByBoardIdWithMember(boardId);
    if (!optionalBoard.isPresent()) {
      return null;
    }

    Board board = optionalBoard.get();
//    board.plusViewNum(board.getHit());
    boardRepository.addViewHit(boardId);
    return board;
  }

  @Transactional
  public void softDeleteBoard(Long boardId) {
//    boardRepository.deleteById(boardId);
    Board board = boardRepository.getById(boardId);
    board.changeAvailableBoard(false);
  }


  @Transactional
  public void updateBoard(Board board) {
    Board one = boardRepository.getById(board.getId());
    one.changeBoard(board);
  }


  //////////Security
  public boolean isAuthorityAtBoardUpdateAndDelete(HttpServletRequest request, Board board) {

    Member member = memberService.getMemberByRequestJwt(request);


    if (member.getRole().equals("ADMIN")) {
      return true;
    } else if (board.getMember().getId().equals(member.getId())) {
      return true;
    } else {
      return false;
    }

  }

  //boardType에 따른 권한
  public boolean isAuthorityAtBoardWrite(String boardType, Member member) {

    if (member.getRole().equals("ADMIN")) {
      return true;
    } else if (boardType.equals("qna") && member.getRole().equals("MEMBER")) {
      return true;
    }else{
      return false;
    }

  }

  public Page<BoardPageDto> findAllPageBySearch(BoardSearchCondition condition, Pageable pageable) {
    return boardRepository.findAllPageByKeyword(condition, pageable);
  }
}

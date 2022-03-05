package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.dto.board.BoardPageDto;
import com.jungwoo.apiserver.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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
  private final ImageTraceService imageTraceService;

  //db초기화용.
  @Transactional
  public void saveWithMember(Board board, Member member) {

    board.setMember(member);
    boardRepository.save(board);
  }

  @Transactional(readOnly = true)
  public Page<BoardPageDto> findPageSort(String boardType, Pageable pageable) {
    return boardRepository.findAllPageSort(boardType, pageable);
  }


  @Transactional
  public Long createBoard(Board board) throws IOException {

    boardRepository.save(board);

    imageTraceService.permanentSaveImage(board.getMember().getLoginId(), board);

    return board.getId();
  }

  @Transactional(readOnly = true)
  public Board getBoardById(Long boardId) {
    return boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
  }

  public Optional<Board> getBoardWithMemberById(Long boardId) {
    return boardRepository.findByBoardIdWithMember(boardId);
  }

  public List<Board> getBoardsAll() {
    return boardRepository.findAll();
  }


  @Transactional
  public Board getBoardDetail(Long boardId) {

    Optional<Board> optionalBoard = boardRepository.findByBoardIdWithMember(boardId);
    if (!optionalBoard.isPresent()) {
      return null;
    }

    Board board = optionalBoard.get();
//    board.plusViewNum(board.getHit());
    boardRepository.plusBoardHit(boardId);
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
}

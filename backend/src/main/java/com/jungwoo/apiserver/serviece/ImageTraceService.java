package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Board;
import com.jungwoo.apiserver.domain.ImageTrace;
import com.jungwoo.apiserver.repository.ImageTraceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * fileName     : FileLogService
 * author       : jungwoo
 * description  :q
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageTraceService {

  private final ImageTraceRepository imageTraceRepository;


  @Transactional
  public void temporarySave(ImageTrace temp) {
    imageTraceRepository.save(temp);
  }

  @Transactional
  public void permanentSaveImage(String loginId, Board board) throws IOException {

    //영구저장할 새로운 절대 경로 디렉토리를 생성.
    String newAbsoulteDirPath = makeDirWithBoardId(board);

    //ImageTrace dirty checking
    List<ImageTrace> imageTraceList = imageTraceRepository.findAllByLoginId(loginId);
    for (ImageTrace i : imageTraceList) {

      i.changeBoard(board);
      i.changeUseFlag(true);
      i.changeFilePath("/img/boardImage/" + board.getId() + "/" + i.getFileUUID());


      String newFilePath = newAbsoulteDirPath + "/" + i.getFileUUID();
      moveImageFile(i.getFileAbsolutePath(), newFilePath);
      i.changeFileAbsolutePath(newFilePath);
    }



  }

  //현재 파일 전체 Path, 이동할 파일 전체 Path.
  public void moveImageFile(String currentFilePath, String newFilePath) throws IOException {

    log.info(currentFilePath);
    log.info(newFilePath);

    Path oldFile = Paths.get(currentFilePath);
    Path newFile = Paths.get(newFilePath);
    Path newPath = Files.move(oldFile, newFile);
  }

  public String makeDirWithBoardId(Board board) {
    String path = "/Users/jungwoo/Desktop/dev/SpringBootApiServer/frontend/public/img/boardImage/";
    path = path + board.getId();

    File folder = new File(path);

    folder.mkdir();

    return path;
  }


  @Transactional
  public void deleteTempImageByLoginId(String loginId) {


    List<ImageTrace> allByLoginId = imageTraceRepository.findAllByLoginId(loginId);
    for (ImageTrace i : allByLoginId) {

      //파일삭제
      File file = new File(i.getFileAbsolutePath());
      file.delete();

      //db row 삭제
      imageTraceRepository.deleteById(i.getId());

    }
  }
}

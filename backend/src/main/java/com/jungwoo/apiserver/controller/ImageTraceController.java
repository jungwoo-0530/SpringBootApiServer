package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.ImageTrace;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.ImageTraceService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * fileName     : FileLogController
 * author       : jungwoo
 * description  :
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageTraceController {

  private final ImageTraceService imageTraceService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;


  @SneakyThrows
  @PostMapping("/image/upload")
  public ImageDto imageUploadTest(MultipartHttpServletRequest multipartReq) throws IOException {

    log.info("===========imageUploadTest===========================");


    MultipartFile file = multipartReq.getFile("upload");

    String userId = jwtAuthenticationProvider.getUserPk(
        jwtAuthenticationProvider.getTokenInRequestHeader(multipartReq, "Bearer"));

    String TempImageAbsolutePath = "/Users/jungwoo/Desktop/dev/SpringBootApiServer/frontend/public/img/tempImage/";

    String fileOriginalName = file.getOriginalFilename();
    UUID uu = UUID.randomUUID();
    String newUUID = uu + fileOriginalName;
    String path = "/img/tempImage/" + newUUID;
    ImageTrace temp = ImageTrace.builder().
        fileName(fileOriginalName).
        useFlag(false).
        fileUUID(newUUID).
        filePath(path).
        fileAbsolutePath(TempImageAbsolutePath + "/" + newUUID).
        userPk(userId).
        build();


    imageTraceService.temporarySave(temp);


    File dest = new File(TempImageAbsolutePath + newUUID);

    file.transferTo(dest);


    return ImageDto.builder().uploaded(true).url(path).build();

  }

  @Getter
  @Builder
  public static class ImageDto {

    boolean uploaded;
    String url;
  }


  @PostMapping("/image/delete")
  public ResponseEntity<? extends BasicResponse> deleteTempImage(HttpServletRequest request) {

    String loginId = jwtAuthenticationProvider.getUserPk(jwtAuthenticationProvider.getTokenInRequestHeader(request,"Bearer"));

    imageTraceService.deleteTempImageByLoginId(loginId);

    return ResponseEntity.status(201).body(new CommonResponse<>(loginId, "임시 이미지 삭제 완료"));
  }
}

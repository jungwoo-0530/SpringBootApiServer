package com.jungwoo.apiserver.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * fileName     : BoardPageDto
 * author       : jungwoo
 * description  :
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BoardPageDto {
  private Long id;
  private String title;
  private String author;
  private String content;
  private String type;
  private Integer hit;
  private boolean available;
  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;


  @QueryProjection
  public BoardPageDto(Long id, String title, String author, String content, String type, Integer hit, boolean available, ZonedDateTime createDate, ZonedDateTime updateDate) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.content = content;
    this.type = type;
    this.hit = hit;
    this.available = available;
    this.createDate = createDate;
    this.updateDate = updateDate;

  }

}

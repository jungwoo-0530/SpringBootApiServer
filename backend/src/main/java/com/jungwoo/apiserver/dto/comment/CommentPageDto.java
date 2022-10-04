package com.jungwoo.apiserver.dto.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;

/**
 * fileName     : CommentPageDto
 * author       : jungwoo
 * description  :
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentPageDto {

  private Long id;

  private boolean available;

  private boolean editable;

  private String author;

  private String content;

  private Long deep;


  private Long parentId;

  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;

  @QueryProjection
  public CommentPageDto(Long id, boolean available, boolean editable, String author, String content, Long parentId, Long deep, ZonedDateTime createDate, ZonedDateTime updateDate) {
    this.id = id;
    this.available = available;
    this.editable = editable;
    this.author = author;
    this.content = content;
    this.parentId = parentId;
    this.deep = deep;
    this.createDate = createDate;
    this.updateDate = updateDate;
  }


}

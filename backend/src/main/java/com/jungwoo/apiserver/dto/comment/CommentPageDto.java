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

  private String author;

  private String content;

  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;

  @QueryProjection
  public CommentPageDto(Long id, boolean available, String author, String content, ZonedDateTime createDate, ZonedDateTime updateDate) {
    this.id = id;
    this.available = available;
    this.author = author;
    this.content = content;
    this.createDate = createDate;
    this.updateDate = updateDate;
  }

}

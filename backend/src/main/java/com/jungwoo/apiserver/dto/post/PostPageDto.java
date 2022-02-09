package com.jungwoo.apiserver.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * fileName     : PostPageDto
 * author       : jungwoo
 * description  :
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostPageDto {
  private Long id;
  private String title;
  private String author;
  private String content;
  private String type;
  private Long hit;
  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;


  @QueryProjection
  public PostPageDto(Long id, String title, String author, String content, String type, Long hit, ZonedDateTime createDate, ZonedDateTime updateDate) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.content = content;
    this.type = type;
    this.hit = hit;
    this.createDate = createDate;
    this.updateDate = updateDate;

  }

}

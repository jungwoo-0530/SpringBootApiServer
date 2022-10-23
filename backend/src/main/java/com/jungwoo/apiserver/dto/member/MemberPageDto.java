package com.jungwoo.apiserver.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * fileName     : MemberPageDto
 * author       : jungwoo
 * description  :
 */
@Data
@NoArgsConstructor
@Builder
public class MemberPageDto {

  private Long id;

  private String loginId;

  private String name;

  private String role;

  private ZonedDateTime createDate;



  @QueryProjection
  public MemberPageDto(Long id, String loginId, String name, String role, ZonedDateTime createDate) {
    this.id = id;
    this.loginId = loginId;
    this.name = name;
    this.role = role;
    this.createDate = createDate;
  }
}

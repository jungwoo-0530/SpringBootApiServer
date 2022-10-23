package com.jungwoo.apiserver.dto.detect;

import lombok.*;

import java.time.ZonedDateTime;

/**
 * fileName     : detectDto
 * author       : jungwoo
 * description  :
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetectDto {

  String email;
  String keyword;
  String userId;
  ZonedDateTime createDate;
  ZonedDateTime completeDate;
}

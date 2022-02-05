package com.jungwoo.apiserver.Dto.Member;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberRequest {

  private String loginId;
  private String password;
  private String name;
  private String email;
  private String telephone;

}

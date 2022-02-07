package com.jungwoo.apiserver.domain;

import lombok.*;
import javax.persistence.*;
/**
 * fileName     : Member
 * author       : jungwoo
 * description  : 회원 Entity
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEMBER_ID")
  private Long id;

  private String loginId;
  private String password;
  private String name;
  private String email;
  private String telephone;
  private String role;


}

package com.jungwoo.apiserver.domain.maria;

import com.jungwoo.apiserver.domain.BaseTimeEntity;
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


  public void change(String name, String telephone){
    this.password = name;
    this.telephone = telephone;
  }

  public void changeRole(String role) {
    this.role = role;
  }

  public void changePassword(String password) {
    this.password = password;
  }




}

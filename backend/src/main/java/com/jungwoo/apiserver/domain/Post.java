package com.jungwoo.apiserver.domain;

import lombok.*;

import javax.persistence.*;

/**
 * fileName     : Post
 * author       : jungwoo
 * description  :
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "POST_ID")
  private Long id;

  private String title;
  private String content;
  private String type;
  private Long hit;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;


  public void plusViewNum(Long hit) {
    this.hit = hit+1;
  }

}

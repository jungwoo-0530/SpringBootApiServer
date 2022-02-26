package com.jungwoo.apiserver.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * fileName     : Board
 * author       : jungwoo
 * description  :
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOARD_ID")
  private Long id;

  private String title;
  private String content;
  private String type;
  private Long hit;

  private boolean available;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;



  public void plusViewNum(Long hit) {
    this.hit = hit+1;
  }

  public void changeBoard(Board board) {
    this.title = board.getTitle();
    this.content = board.getContent();
  }

  public void changeAvailableBoard(boolean flag){
    this.available = flag;
  }


//
//  @PrePersist
//  public void prePersist(){
//    this.available = true;
//  }

}

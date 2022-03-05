package com.jungwoo.apiserver.domain;

import lombok.*;

import javax.persistence.*;

/**
 * fileName     : FileLog
 * author       : jungwoo
 * description  :
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageTrace extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "IMAGETRACE_ID")
  private Long id;

  private boolean useFlag;
  private String fileName;
  private String fileUUID;
  private String filePath;
  private String fileAbsolutePath;
  private String userPk;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID")
  private Board board;


  public void changeBoard(Board board) {
    this.board = board;
  }

  public void changeUseFlag(boolean useFlag) {
    this.useFlag = useFlag;
  }

  public void changeFilePath(String filePath) {
    this.filePath = filePath;
  }

  public void changeFileAbsolutePath(String fileAbsolutePath) {
    this.fileAbsolutePath = fileAbsolutePath;
  }
}

package com.jungwoo.apiserver.domain;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;

  @PrePersist
  public void prePersist(){
    this.createDate = ZonedDateTime.now();
    this.updateDate = ZonedDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updateDate = ZonedDateTime.now();
  }

}

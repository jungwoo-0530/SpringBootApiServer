package com.jungwoo.apiserver.domain.mongo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * fileName     : Keyword
 * author       : jungwoo
 * description  :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "keywords")
public class Keyword{

  @MongoId
  private ObjectId id;

  @Column(name = "user_id")
  private String userId;

  private String keyword;

  private Integer status;

  @Column(name = "create_date")
  private Date createDate;
  @Column(name = "update_date")
  private Date updateDate;

//  @PrePersist
//  public void prePersist(){
//    this.createDate = Date.from(ZonedDateTime.now().toInstant());
//    this.updateDate = Date.from(ZonedDateTime.now().toInstant());
//  }

}

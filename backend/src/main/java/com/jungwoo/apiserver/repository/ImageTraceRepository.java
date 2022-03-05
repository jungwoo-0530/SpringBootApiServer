package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.ImageTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * fileName     : FileLogRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface ImageTraceRepository extends JpaRepository<ImageTrace, Long> {



  @Query("select i from ImageTrace i where i.userPk = :loginId and i.useFlag = false")
  List<ImageTrace> findAllByLoginId(@Param("loginId") String loginId);

  @Query("delete from ImageTrace i where i.userPk = :loginId")
  void deleteByLoginId(@Param("loginId") String loginId);
}

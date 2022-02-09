package com.jungwoo.apiserver.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * fileName     : QuerydslConfig
 * author       : jungwoo
 * description  :
 */
@EnableJpaAuditing
@Configuration
public class QuerydslConfig {

  @PersistenceContext
  private EntityManager entityManager;

  public QuerydslConfig() {
  }

  //JPAQueryFactory를 빈으로 등록함으로써 repository에서 바로 가져와서 사용.
  //JPAQueryFactory가 우리가 작성한 것을 토대로 entityManager를 통하여 질의.
  @Bean
  public JPAQueryFactory jpaQueryFactory(){
    return new JPAQueryFactory(entityManager);
  }


}

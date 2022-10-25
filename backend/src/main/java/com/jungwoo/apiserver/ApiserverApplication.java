package com.jungwoo.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@EnableTransactionManagement
@EnableMongoRepositories(basePackages = "com.jungwoo.apiserver.repository.mongo")
@EnableJpaRepositories(basePackages = "com.jungwoo.apiserver.repository.maria")
public class ApiserverApplication {

  @PostConstruct
  void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
  public static void main(String[] args) {
    SpringApplication.run(ApiserverApplication.class, args);
  }

}

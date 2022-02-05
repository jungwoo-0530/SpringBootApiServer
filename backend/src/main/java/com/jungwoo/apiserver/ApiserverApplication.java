package com.jungwoo.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ApiserverApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiserverApplication.class, args);
  }

}

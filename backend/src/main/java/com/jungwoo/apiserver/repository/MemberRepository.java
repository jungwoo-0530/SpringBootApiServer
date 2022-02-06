package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


  boolean existsByLoginId(String loginId);

  boolean existsByEmail(String email);

  Optional<Member> findByLoginId(String loginId);
}

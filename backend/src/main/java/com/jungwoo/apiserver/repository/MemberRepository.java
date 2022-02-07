package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * fileName     : MemberRepository
 * author       : jungwoo
 * description  : Member Entity에 관하여 DB와 관련된 일을 하는 Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


  boolean existsByLoginId(String loginId);

  boolean existsByEmail(String email);

  Optional<Member> findByLoginId(String loginId);
}

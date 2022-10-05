package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.dto.member.MemberPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * fileName     : MemberRepository
 * author       : jungwoo
 * description  : Member Entity에 관하여 DB와 관련된 일을 하는 Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


  boolean existsByLoginId(String loginId);

  boolean existsByEmail(String email);

  Optional<Member> findByLoginId(String loginId);

}

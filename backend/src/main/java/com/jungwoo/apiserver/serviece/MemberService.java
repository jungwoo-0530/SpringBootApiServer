package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
/**
 * fileName     : MemberService
 * author       : jungwoo
 * description  : Member Entity와 관련된 서비스 계층
 */
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private final PasswordEncoder passwordEncoder;

  public boolean dupLoginIdCheck(String loginId) {
    return memberRepository.existsByLoginId(loginId);
  }

  public boolean dupEmailCheck(String email) {
    return memberRepository.existsByEmail(email);
  }

  @Transactional
  public Long save(Member member) {

    String encodedPassword = passwordEncoder.encode(member.getPassword());
    member.setPassword(encodedPassword);
    memberRepository.save(member);
    return member.getId();

  }

  @Transactional(readOnly = true)
  public Optional<Member> findByLoginId(String loginId) {
    return memberRepository.findByLoginId(loginId);
  }

  @Transactional(readOnly = true)
  public Member getMemberByJwt(String token) {
    return memberRepository.findByLoginId(jwtAuthenticationProvider.getUserPk(token)).orElseThrow(NoSuchElementException::new);
  }

  @Transactional
  public void updateMember(Member member) {
    Member findMember = memberRepository.findByLoginId(member.getLoginId()).orElseThrow(NoSuchElementException::new);
    findMember.change(member.getName(), member.getTelephone());
  }
}

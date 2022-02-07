package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

  public boolean dupLoginIdCheck(String loginId) {
    return memberRepository.existsByLoginId(loginId);
  }

  public boolean dupEmailCheck(String email) {
    return memberRepository.existsByEmail(email);
  }

  @Transactional
  public void save(Member member) {

    String encodedPassword  = passwordEncoder.encode(member.getPassword());
    member.setPassword(encodedPassword);

    memberRepository.save(member);

  }

  @Transactional(readOnly = true)
  public Optional<Member> findByLoginId(String loginId) {
    return memberRepository.findByLoginId(loginId);
  }
}

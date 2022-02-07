package com.jungwoo.apiserver.security;

import com.jungwoo.apiserver.domain.Member;
import com.jungwoo.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    Member member = (Member)memberRepository.findByLoginId(loginId).orElseThrow(() -> {
      return new UsernameNotFoundException("not found loginId : " + loginId);
    });
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
    return new CustomUserDetails(member, authorities);
  }


//  public static boolean hasAdminRole() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//    return authorities.stream().filter((o) -> {
//      return o.getAuthority().equals("ROLE_ADMIN");
//    }).findAny().isPresent();
//  }

}

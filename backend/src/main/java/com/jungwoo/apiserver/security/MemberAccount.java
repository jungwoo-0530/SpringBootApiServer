package com.jungwoo.apiserver.security;

import java.util.Collection;

import com.jungwoo.apiserver.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MemberAccount extends User {
  private final Member member;

  public MemberAccount(Member member, Collection<? extends GrantedAuthority> authorities) {
    super(member.getLoginId(), member.getPassword(), authorities);
    this.member = member;
  }

  @Override
  public String getPassword() {
    return super.getPassword();
  }

  @Override
  public String getUsername() {
    return super.getUsername();
  }

  @Override
  public boolean isEnabled() {
    return super.isEnabled();
  }

  @Override
  public boolean isAccountNonExpired() {
    return super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return super.isCredentialsNonExpired();
  }

  public Member getMember() {
    return this.member;
  }
}

package com.jungwoo.apiserver.security.jwt;

import com.jungwoo.apiserver.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * fileName     : JwtAuthenticationProviderTest
 * author       : jungwoo
 * description  :
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationProviderTest {

  @Mock
  JwtAuthenticationProvider jwtAuthenticationProvider;



  @Test
  @DisplayName("토큰 검증")
  public void 토큰_검증() throws Exception{
    //given
    Member accessMember = new Member(0L, "am7227", "password", "김정우", "am7227@naver.com", "010-1111-1111", "admin");
    String token = jwtAuthenticationProvider.createToken(accessMember.getLoginId());

    //when
    boolean isValidate = jwtAuthenticationProvider.validateToken(token);


    //then
    assertThat(isValidate).isTrue();

  }

}
package com.jungwoo.apiserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungwoo.apiserver.dto.member.CreateMemberRequest;
import com.jungwoo.apiserver.security.CustomUserDetailsService;
import com.jungwoo.apiserver.security.jwt.JwtAccessDeniedHandler;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationEntryPoint;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * fileName     : MemberControllerTest
 * author       : jungwoo
 * description  :
 */
@WebMvcTest(MemberController.class)//web과 관련된 빈만 주입됨.(@Controller, @ControllerAdvice 등), Service, Component는 주입되지 않는다.
@ExtendWith(MockitoExtension.class)//Mockito를 사용하기
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MemberService memberService;

  @MockBean
  private CustomUserDetailsService customUserDetailsService;

  @MockBean
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @MockBean
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @MockBean
  private JwtAccessDeniedHandler jwtAccessDeniedHandler;


  @Test
  @DisplayName("회원가입")
  void 회원가입() throws Exception{
    //given
    CreateMemberRequest reqDto = new CreateMemberRequest("am7227", "password", "김정우", "am7227@naver.com", "010-1111-1111");


    //when
    when(memberService.save(any())).thenReturn(0L);

    //then
    mockMvc.perform(post("/register")
            .content(new ObjectMapper().writeValueAsString(reqDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());

  }




}
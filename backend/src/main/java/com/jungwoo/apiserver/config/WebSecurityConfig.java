package com.jungwoo.apiserver.config;

import com.jungwoo.apiserver.security.CustomUserDetailsService;
import com.jungwoo.apiserver.security.jwt.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * fileName     : WebSecurityConfig
 * author       : jungwoo
 * description  : Spring Security를 사용하기 위한 Config Class
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService customUserDetailService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }




  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .and()
        .authorizeRequests()
        .antMatchers("/register","/login").permitAll()
        .anyRequest().authenticated()//permitAll을 제외한 것은 인증 시스템을 사용
        .and()
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.
        addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);
    //UsernamePasswordAuthenticationFilter가 기본 인증 시스템. 저것 전 JwtAuthenticationFilter을 사용하겠다는 것.


  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
  }

  @Bean
  RoleHierarchy roleHierarchy(){
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_SUBSCRIBER > ROLE_MEMBER");
    return roleHierarchy;
  }

//permitAll을 한다고 필터를 적용안하는 것이 아니라 응답은 정상적으로 감.
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/posts/qna","/swagger-ui/index", "/test/qna");
    web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
    web.ignoring().mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v3/api-docs","/webjars/**",
        "/swagger-ui", "/swagger-ui/**");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}

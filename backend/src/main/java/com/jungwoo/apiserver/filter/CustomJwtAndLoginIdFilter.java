//package com.jungwoo.apiserver.filter;
//
//import com.jungwoo.apiserver.domain.Board;
//import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
//import com.jungwoo.apiserver.serviece.BoardService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Optional;
//
///**
// * fileName     : CustomJwtAndLoginIdFilter
// * author       : jungwoo
// * description  : DELETE, PUT /board/{boardId} 해당 board의 author와 token에 있는 loginId를 체크한다.
// */
//
//@RequiredArgsConstructor
//public class CustomJwtAndLoginIdFilter extends OncePerRequestFilter {
//
//  private final BoardService boardService;
//  private final JwtAuthenticationProvider jwtAuthenticationProvider;
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//    if((reqMethod.equals("DELETE")|| reqMethod.equals("PUT") ) && reqURI.matches("/boards/([0-9]*|$)")) {
//
//      String loginId = jwtAuthenticationProvider.getUserPk(jwtAuthenticationProvider.getTokenInRequestHeader(request, "Bearer"));
//
//      String reqURI = request.getRequestURI();
//      String reqMethod = request.getMethod();
//
//      String[] split = reqURI.split("/");
//      String boardId = split[1];
//
//      String loginIdByBoard = boardService.getBoardWithMemberById(Long.getLong(boardId)).get().getMember().getLoginId();
//
//      if(loginIdByBoard.equals(loginId)){
//
//      }
//
//    }
//
//
//    filterChain.doFilter(request,response);
//
//  }
//
//}

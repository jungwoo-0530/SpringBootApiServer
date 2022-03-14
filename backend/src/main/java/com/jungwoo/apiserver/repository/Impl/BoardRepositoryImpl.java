package com.jungwoo.apiserver.repository.Impl;

import com.jungwoo.apiserver.dto.board.BoardPageDto;
import com.jungwoo.apiserver.dto.board.QBoardPageDto;
import com.jungwoo.apiserver.repository.BoardRepositoryCustom;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.jungwoo.apiserver.domain.QBoard.board;

/**
 * fileName     : BoardRepositoryImpl
 * author       : jungwoo
 * description  :
 */
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Page<BoardPageDto> findAllPageSort(String pageType, Pageable pageable) {
    JPAQuery<BoardPageDto> query = jpaQueryFactory
        .select(new QBoardPageDto(
            board.id.as("id"),
            board.title.as("title"),
            board.member.loginId.as("author"),
            board.content.as("content"),
            board.type.as("type"),
            board.hit.as("hit"),
            board.available.as("available"),
            board.createDate.as("createDate"),
            board.updateDate.as("updateDate")
        ))
        .where(board.type.eq(pageType), board.available.eq(true))
        .from(board)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());
    //데이터 정렬을 OrderSpecifier를 사용해서 편리하게 사용 가능.
    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
      query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    long total = jpaQueryFactory
        .select(board)
        .from(board)
        .where(board.type.eq(pageType))
        .fetch().size();

//    List<BoardPageDto> content = results.getResults();

    List<BoardPageDto> content = query.fetch();
    return new PageImpl<>(content, pageable, total);

  }
}

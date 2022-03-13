package com.jungwoo.apiserver.repository.Impl;

import com.jungwoo.apiserver.dto.comment.CommentPageDto;
import com.jungwoo.apiserver.dto.comment.QCommentPageDto;
import com.jungwoo.apiserver.repository.CommentRepositoryCustom;
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

import static com.jungwoo.apiserver.domain.QComment.comment;

/**
 * fileName     : CommentRepositoryImpl
 * author       : jungwoo
 * description  :
 */
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<CommentPageDto> findAllPageSort(Long boardId, Pageable pageable) {
    JPAQuery<CommentPageDto> query = jpaQueryFactory
        .select(new QCommentPageDto(
            comment.id.as("id"),
            comment.available.as("available"),
            comment.member.loginId.as("author"),
            comment.content.as("content"),
            comment.createDate.as("createDate"),
            comment.updateDate.as("updateDate")
        ))
        .where(comment.board.id.eq(boardId))
        .from(comment)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());
    //데이터 정렬을 OrderSpecifier를 사용해서 편리하게 사용 가능.
    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder(comment.getType(), comment.getMetadata());
      query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    long total = jpaQueryFactory
        .select(comment)
        .from(comment)
        .where(comment.board.id.eq(boardId))
        .fetch().size();


    List<CommentPageDto> content = query.fetch();
    return new PageImpl<>(content, pageable, total);

  }
}

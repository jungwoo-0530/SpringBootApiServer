package com.jungwoo.apiserver.repository.Impl;

import com.jungwoo.apiserver.dto.post.PostPageDto;
import com.jungwoo.apiserver.dto.post.QPostPageDto;
import com.jungwoo.apiserver.repository.PostRepositoryCustom;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
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

import static com.jungwoo.apiserver.domain.QMember.member;
import static com.jungwoo.apiserver.domain.QPost.post;
/**
 * fileName     : PostRepositoryImpl
 * author       : jungwoo
 * description  :
 */
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Page<PostPageDto> findAllPageSort(String pageType,Pageable pageable) {
    JPAQuery<PostPageDto> query = jpaQueryFactory
        .select(new QPostPageDto(
            post.id.as("id"),
            post.title.as("title"),
            post.member.loginId.as("author"),
            post.content.as("content"),
            post.type.as("type"),
            post.hit.as("hit"),
            post.createDate.as("createDate"),
            post.updateDate.as("updateDate")
        ))
        .where(post.type.eq(pageType))
        .from(post)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());
    //데이터 정렬을 OrderSpecifier를 사용해서 편리하게 사용 가능.
    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
      query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    long total = jpaQueryFactory
        .select(post)
        .from(post)
        .where(post.type.eq(pageType))
        .fetch().size();

//    List<PostPageDto> content = results.getResults();

    List<PostPageDto>content = query.fetch();
    return new PageImpl<>(content, pageable, total);

  }
}

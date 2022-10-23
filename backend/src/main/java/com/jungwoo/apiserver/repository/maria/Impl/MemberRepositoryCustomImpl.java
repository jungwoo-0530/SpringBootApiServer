package com.jungwoo.apiserver.repository.maria.Impl;

import com.jungwoo.apiserver.dto.member.MemberPageDto;
import com.jungwoo.apiserver.dto.member.QMemberPageDto;
import com.jungwoo.apiserver.repository.maria.MemberRepositoryCustom;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jungwoo.apiserver.domain.maria.QMember.member;


/**
 * fileName     : MemberRepositoryImpl
 * author       : jungwoo
 * description  :
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  @Override
  public Page<MemberPageDto> findAllPageSort(Pageable pageable) {

    QMemberPageDto qMemberPageDto = new QMemberPageDto(
        member.id.as("id"),
        member.loginId.as("loginId"),
        member.name.as("name"),
        member.role.as("role"),
        member.createDate.as("createDate"));

    JPAQuery<MemberPageDto> query = jpaQueryFactory
        .select(qMemberPageDto)
        .orderBy(
            provideStatusOrder(),
            member.id.asc()
        )
        .from(member)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    long total = jpaQueryFactory
        .select(member)
        .from(member)
        .fetch().size();


    List<MemberPageDto> content = query.fetch();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public Page<MemberPageDto> findAllPageSortBySearch(Pageable pageable, String searchWord) {
    QMemberPageDto qMemberPageDto = new QMemberPageDto(
        member.id.as("id"),
        member.loginId.as("loginId"),
        member.name.as("name"),
        member.role.as("role"),
        member.createDate.as("createDate"));

    JPAQuery<MemberPageDto> query = jpaQueryFactory
        .select(qMemberPageDto)
        .where(member.loginId.like(searchWord))
        .orderBy(
            provideStatusOrder(),
            member.id.asc()
        )
        .from(member)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    long total = jpaQueryFactory
        .select(member)
        .from(member)
        .fetch().size();


    List<MemberPageDto> content = query.fetch();

    return new PageImpl<>(content, pageable, total);

  }


  private OrderSpecifier<Integer> provideStatusOrder() {
    NumberExpression<Integer> cases = new CaseBuilder()
        .when(member.role.eq("ADMIN"))
        .then(1)
        .when(member.role.eq("SUBSCRIBER"))
        .then(2)
        .when(member.role.eq("MEMBER"))
        .then(3)
        .otherwise(4);
    return new OrderSpecifier<>(Order.ASC, cases);
  }
}

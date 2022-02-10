package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Post;
import jdk.nashorn.internal.ir.Optimistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * fileName     : PostRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>,PostRepositoryCustom {


  @Query("select p from Post p left join fetch p.member where p.id = :id")
  Optional<Post> findByPostIdWithMember(@Param("id") Long postId);
}

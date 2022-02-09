package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * fileName     : PostRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>,PostRepositoryCustom {
}

package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * fileName     : CommentRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{

}

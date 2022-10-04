package com.jungwoo.apiserver.repository;

import com.jungwoo.apiserver.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * fileName     : CommentRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{

}

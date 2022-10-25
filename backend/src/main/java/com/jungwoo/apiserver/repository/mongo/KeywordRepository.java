package com.jungwoo.apiserver.repository.mongo;

import com.jungwoo.apiserver.domain.mongo.Keyword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.security.Key;

/**
 * fileName     : KeywordRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface KeywordRepository extends MongoRepository<Keyword, String> {

}

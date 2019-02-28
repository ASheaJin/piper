package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Token;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository {
    Token selectByToken(@Param("token") String token);

    Token getTokenByUserId(@Param("userId") String userId);

    void addToken(Token token);
}

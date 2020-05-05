package com.myhome.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    TokenEntity findByUserId(String userId);

    boolean existsByUserId(@Param("userId") String userId);

    TokenEntity findByRefreshToken(String refreshToken);

    void deleteByUserId(String userId);
}

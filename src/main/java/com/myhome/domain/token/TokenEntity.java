package com.myhome.domain.token;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;



@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Getter
@Table(name="tbl_token")
@Entity
public class TokenEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "token_idx")
    private int tokenIdx;

    @Column(nullable = false, unique = true, length = 100)
    private String userId;

    @Column(nullable=false)
    private String refreshToken;

    @Column(nullable=false)
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column(nullable=false)
    private LocalDateTime expiredDatetime;

    public TokenEntity update(String refreshToken, LocalDateTime expiredDatetime){
        this.refreshToken = refreshToken;
        this.expiredDatetime = expiredDatetime;

        return this;
    }

    @Builder
    public TokenEntity(String userId, String refreshToken, LocalDateTime createdDatetime, LocalDateTime expiredDatetime) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.createdDatetime = createdDatetime;
        this.expiredDatetime = expiredDatetime;
    }
}




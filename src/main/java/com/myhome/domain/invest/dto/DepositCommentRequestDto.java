package com.myhome.domain.invest.dto;

import com.myhome.domain.invest.DepositCommentEntity;
import lombok.Getter;

@Getter
public class DepositCommentRequestDto {
    private String contents;

    public DepositCommentEntity toEntity() {
        return DepositCommentEntity.builder()
                .contents(contents)
                .build();
    }
}

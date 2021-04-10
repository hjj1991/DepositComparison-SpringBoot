package com.myhome.domain.invest.dto;

import com.myhome.domain.invest.DepositCommentEntity;
import com.myhome.domain.invest.InstallmentSavingCommentEntity;
import lombok.Getter;

@Getter
public class InstallmentSavingCommentRequestDto {
    private String contents;

    public InstallmentSavingCommentEntity toEntity() {
        return InstallmentSavingCommentEntity.builder()
                .contents(contents)
                .build();
    }
}

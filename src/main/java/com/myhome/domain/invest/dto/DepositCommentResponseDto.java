package com.myhome.domain.invest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class DepositCommentResponseDto {
    private String contents;
    private Long commentIdx;
    private String creatorId;
    private LocalDateTime createdDatetime;


}

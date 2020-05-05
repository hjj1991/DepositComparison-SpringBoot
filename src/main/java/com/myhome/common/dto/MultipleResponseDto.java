package com.myhome.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleResponseDto<T> extends CommonResponseDto {
    private List<T> list;
}

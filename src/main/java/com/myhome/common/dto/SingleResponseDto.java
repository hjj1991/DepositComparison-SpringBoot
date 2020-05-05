package com.myhome.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponseDto<T> extends CommonResponseDto {
    private T data;
}

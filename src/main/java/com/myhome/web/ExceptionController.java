package com.myhome.web;

import com.myhome.common.dto.CommonResponseDto;
import com.myhome.common.exception.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
    @GetMapping(value = "/entrypoint")
    public CommonResponseDto entrypointException() {
        throw new CAuthenticationEntryPointException();
    }
}
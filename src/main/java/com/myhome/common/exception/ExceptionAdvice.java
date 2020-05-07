package com.myhome.common.exception;

import com.myhome.common.dto.CommonResponseDto;
import com.myhome.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    protected CommonResponseDto userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("-1")), getMessage("사용자가 존재하지 않습니다."));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.OK)
    protected CommonResponseDto passwordNotMatchExcepiton(HttpServletRequest request, PasswordNotMatchException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("-1")), getMessage("비밀번호가 일치하지 않습니다."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponseDto accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("-1")), getMessage("토큰이 만료되었습니다."));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResponseDto authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("-1")), getMessage("인증이 필요합니다."));
    }

    @ExceptionHandler(CCommunicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponseDto communicationException(HttpServletRequest request, CCommunicationException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("-1")), getMessage("통신중 오류가 발생하였습니다."));
    }

    private String getMessage(String string) {
        // TODO Auto-generated method stub
        return string;
    }


}
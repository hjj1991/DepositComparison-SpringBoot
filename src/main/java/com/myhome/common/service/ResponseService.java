package com.myhome.common.service;

import com.myhome.common.dto.CommonResponseDto;
import com.myhome.common.dto.MultipleResponseDto;
import com.myhome.common.dto.SingleResponseDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    @Getter
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니디."), FAIL(-1, "실패하였습니다.");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResponseDto<T> getSingleResponse(T data) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 성공, 실패 출력하지 않음
    public <T> SingleResponseDto<T> getSingleResponse2(T data) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);

        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> MultipleResponseDto<T> getMultipleResponseDto(List<T> list) {
        MultipleResponseDto<T> result = new MultipleResponseDto<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // 성공 결과만 처리하는 메소드
    public CommonResponseDto getSuccessResult() {
        CommonResponseDto result = new CommonResponseDto();
        setSuccessResult(result);
        return result;
    }

    // 실패 결과만 처리하는 메소드
    public CommonResponseDto getFailResult(int code, String msg) {
        CommonResponseDto result = new CommonResponseDto();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResponseDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 결과 모델에 api 요청 실패 데이터를 세팅해주는 메소드
    private void setFailResult(CommonResponseDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResponseDto<T> getSingleResponseDto(T data, int code, String msg, boolean success) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }
    // data가 존재하지 않는 결과를 처리하는 메소드
    public <T> SingleResponseDto<T> getNotDataSingleResponseDto(int code, String msg, boolean success) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }
}

package com.myhome.web;


import com.myhome.common.dto.MultipleResponseDto;
import com.myhome.common.dto.SingleResponseDto;
import com.myhome.common.service.ResponseService;
import com.myhome.domain.invest.InstallmentSavingRepository;
import com.myhome.domain.invest.dto.*;
import com.myhome.domain.invest.service.InstallmentSavingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = { "3. InstallmentSaving" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class InstallmentSavingController {

    private final InstallmentSavingService installmentSavingService;
    private final InstallmentSavingRepository installmentSavingRepository;
    private final ResponseService responseService;

    @ApiOperation(value = "적금목록 조회", notes = "")
    @GetMapping(value = "/insmoney")
    public MultipleResponseDto<InstallmentResponseDto> findInstallmentSaving(
            @RequestParam(value = "size", required = false, defaultValue = "")String size) {

        return responseService.getMultipleResponseDto(installmentSavingService.findInstallmentSaving(size));
    }

    @ApiOperation(value = "댓글목록 조회", notes = "")
    @GetMapping(value = "/insmoney/comments/list/{installmentSavingId}")
    public MultipleResponseDto<InstallmentSavingCommentResponseDto> findComments(@PathVariable Long installmentSavingId){
        return responseService.getMultipleResponseDto(installmentSavingService.findInstallmentSavingComments(installmentSavingId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "댓글등록", notes = "")
    @PostMapping(value = "/insmoney/comments/add/{installmentSavingId}")
    public SingleResponseDto addComment(@PathVariable Long installmentSavingId, @RequestHeader("X_AUTH_TOKEN") String authToken, @RequestBody InstallmentSavingCommentRequestDto installmentSavingCommentRequestDto){
        HashMap<String, String> result = new HashMap<>();
        result = installmentSavingService.addInstallmentSavingComment(installmentSavingId, authToken, installmentSavingCommentRequestDto);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "댓글수정", notes = "")
    @PutMapping(value = "/insmoney/comments/modify/{commentId}")
    public SingleResponseDto modifyComment(@PathVariable Long commentId, @RequestHeader("X_AUTH_TOKEN") String authToken, @RequestBody InstallmentSavingCommentRequestDto installmentSavingCommentRequestDto){
        HashMap<String, String> result = new HashMap<>();
        result = installmentSavingService.modifyInstallmentSavingComment(commentId, authToken, installmentSavingCommentRequestDto);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "댓글삭제", notes = "")
    @DeleteMapping(value = "/insmoney/comments/delete/{commentId}")
    public SingleResponseDto deleteComment(@PathVariable Long commentId, @RequestHeader("X_AUTH_TOKEN") String authToken){
        HashMap<String, String> result = new HashMap<>();
        result = installmentSavingService.deleteInstallmentSavingComment(commentId, authToken);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }
}

package com.myhome.web;


import com.myhome.common.dto.MultipleResponseDto;
import com.myhome.common.dto.SingleResponseDto;
import com.myhome.common.service.ResponseService;
import com.myhome.domain.invest.DepositRepository;
import com.myhome.domain.invest.InstallmentSavingRepository;
import com.myhome.domain.invest.dto.DepositCommentRequestDto;
import com.myhome.domain.invest.dto.DepositCommentResponseDto;
import com.myhome.domain.invest.dto.DepositResponseDto;
import com.myhome.domain.invest.dto.InstallmentResponseDto;
import com.myhome.domain.invest.service.DepositService;
import com.myhome.domain.invest.service.InstallmentSavingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = { "4. Deposit" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class DepositController {

    private final DepositService depositService;
    private final DepositRepository depositRepository;
    private final ResponseService responseService;

    @ApiOperation(value = "예금목록 조회", notes = "")
    @GetMapping(value = "/depositmoney")
    public MultipleResponseDto<DepositResponseDto> findDeposit(
            @RequestParam(value = "size", required = false, defaultValue = "")String size) {

        return responseService.getMultipleResponseDto(depositService.findDeposit(size));
    }

    @ApiOperation(value = "댓글목록 조회", notes = "")
    @GetMapping(value = "/depositmoney/comments/{depositId}")
    public MultipleResponseDto<DepositCommentResponseDto> findComments(@PathVariable Long depositId){
        return responseService.getMultipleResponseDto(depositService.findDepositComments(depositId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "댓글등록", notes = "")
    @PostMapping(value = "/depositmoney/comments/{depositId}")
    public SingleResponseDto addComment(@PathVariable Long depositId, @RequestHeader("X_AUTH_TOKEN") String authToken, @RequestBody DepositCommentRequestDto depositCommentRequestDto){
        HashMap<String, String> result = new HashMap<>();
        result = depositService.addDepositComment(depositId, authToken, depositCommentRequestDto);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "댓글수정", notes = "")
    @PatchMapping(value = "/depositmoney/comments/{commentId}")
    public SingleResponseDto modifyComment(@PathVariable Long commentId, @RequestHeader("X_AUTH_TOKEN") String authToken, @RequestBody DepositCommentRequestDto depositCommentRequestDto){
        HashMap<String, String> result = new HashMap<>();
        result = depositService.modifyDepositComment(commentId, authToken, depositCommentRequestDto);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "댓글삭제", notes = "")
    @DeleteMapping(value = "/depositmoney/comments/{commentId}")
    public SingleResponseDto deleteComment(@PathVariable Long commentId, @RequestHeader("X_AUTH_TOKEN") String authToken){
        HashMap<String, String> result = new HashMap<>();
        result = depositService.deleteDepositComment(commentId, authToken);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }
}

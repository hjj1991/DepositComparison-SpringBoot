package com.myhome.web;


import com.myhome.common.dto.MultipleResponseDto;
import com.myhome.common.service.ResponseService;
import com.myhome.domain.invest.DepositRepository;
import com.myhome.domain.invest.InstallmentSavingRepository;
import com.myhome.domain.invest.dto.DepositResponseDto;
import com.myhome.domain.invest.dto.InstallmentResponseDto;
import com.myhome.domain.invest.service.DepositService;
import com.myhome.domain.invest.service.InstallmentSavingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = { "4. Deposit" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class DepositController {

    private final DepositService depositService;
    private final DepositRepository depositRepository;
    private final ResponseService responseService;

    @ApiOperation(value = "적금목록 조회", notes = "")
    @GetMapping(value = "/depositmoney")
    public MultipleResponseDto<DepositResponseDto> findDeposit() {

        return responseService.getMultipleResponseDto(depositService.findDeposit());
    }
}

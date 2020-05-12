package com.myhome.web;


import com.myhome.common.dto.MultipleResponseDto;
import com.myhome.common.service.ResponseService;
import com.myhome.domain.invest.InstallmentSavingRepository;
import com.myhome.domain.invest.dto.InstallmentResponseDto;
import com.myhome.domain.invest.service.InstallmentSavingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public MultipleResponseDto<InstallmentResponseDto> findInstallmentSaving() {

        return responseService.getMultipleResponseDto(installmentSavingService.findInstallmentSaving());
    }
}

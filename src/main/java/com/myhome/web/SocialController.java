package com.myhome.web;

import com.myhome.common.dto.SingleResponseDto;
import com.myhome.common.service.ResponseService;
import com.myhome.config.auth.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = { "2. Social" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/social")
public class SocialController {

    private final ResponseService responseService;


    private final KakaoService kakaoService;
    /**
     * 카카오 인증후 리다이렉트 화면
     */
    @ApiOperation(value = "카카오로그인", notes = "카카오계정으로 로그인 & 회원가입을 한다.")
    @PostMapping(value = "/kakao/login")
    public SingleResponseDto<HashMap<String, String>> loginToKakao(@RequestBody HashMap<String, Object> requestBody) throws Exception {
        return responseService.getSingleResponse(kakaoService.getKakaoTokenInfo(requestBody.get("code").toString()));

    }

}

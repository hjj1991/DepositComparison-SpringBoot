package com.myhome.web;

import com.myhome.common.dto.SingleResponseDto;
import com.myhome.common.service.ResponseService;
import com.myhome.config.auth.SocialLoginService;
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


    private final SocialLoginService socialLoginService;
    /**
     * 소셜 로그인 인증후 리다이렉트 화면
     */
    @ApiOperation(value = "소셜로그인", notes = "소셜계정으로 로그인 & 회원가입을 한다.")
    @PostMapping(value = "/login")
    public SingleResponseDto<HashMap<String, String>> loginToSocial(@RequestBody HashMap<String, Object> requestBody) throws Exception {
        String socialType = requestBody.get("provider").toString();
        SingleResponseDto<HashMap<String, String>> result = new SingleResponseDto<HashMap<String, String>>();

        switch (socialType) {
            case "naver":
                result = responseService.getSingleResponse(socialLoginService.getNaverTokenInfo(requestBody.get("code").toString()));
                break;
            case "kakao":
                result = responseService.getSingleResponse(socialLoginService.getKakaoTokenInfo(requestBody.get("code").toString()));
                break;
        }

        return result;
    }

}

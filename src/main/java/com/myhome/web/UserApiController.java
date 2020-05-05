package com.myhome.web;


import com.myhome.common.dto.CommonResponseDto;
import com.myhome.common.dto.SingleResponseDto;
import com.myhome.common.exception.CUserNotFoundException;
import com.myhome.common.exception.PasswordNotMatchException;
import com.myhome.common.service.ResponseService;
import com.myhome.config.security.JwtTokenProvider;
import com.myhome.domain.user.UserEntity;
import com.myhome.domain.user.UserRepository;
import com.myhome.domain.user.dto.UserDetailResponseDto;
import com.myhome.domain.user.dto.UserSignInRequestDto;
import com.myhome.domain.user.dto.UserSignRequestDto;
import com.myhome.service.user.SignService;
import com.myhome.service.user.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = { "1. User" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserApiController {

    private final SignService signService;
    private final ResponseService responseService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResponseDto signup(@RequestBody @Valid UserSignRequestDto userSignRequestDto, BindingResult result) {
        // Srping 인터페이스 유효성 검사 진행 Validator
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            HashMap<String, String> errorList = new HashMap<String, String>();
            for (FieldError error : errors) {
                errorList.put(error.getField(), error.getDefaultMessage());
                // System.out.println (error.getField() + " - " + error.getDefaultMessage());
            }
            return responseService.getFailResult(-1, errorList.toString());
        }
        try{
            signService.signUp(userSignRequestDto);
        }catch (Exception e){
            return responseService.getFailResult(-1, "회원가입 실패하였습니다.");
        }
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "로그인", notes = "아이디로 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResponseDto signin(@RequestBody UserSignInRequestDto userSignInDto) throws Exception {
        UserEntity userEntity = userRepository.findByUserId(userSignInDto.getUserId()).orElseThrow(() -> new CUserNotFoundException());
        if(!passwordEncoder.matches(userSignInDto.getUserPw(), userEntity.getUserPw()))
            throw new PasswordNotMatchException();

        HashMap<String, String> result = signService.signIn(userEntity);

        return responseService.getSingleResponse(result);
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃을 한다.")
    @PostMapping(value = "/signout")
    public SingleResponseDto signout(@RequestHeader("X_REFRESH_TOKEN") String refreshToken) throws Exception {

        HashMap<String, String> result = signService.signOut(refreshToken);
        return responseService.getNotDataSingleResponseDto(Integer.parseInt(result.get("code")), result.get("msg"),
                Boolean.valueOf(result.get("success")).booleanValue());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X_AUTH_TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public SingleResponseDto<UserDetailResponseDto> findUserById(@RequestHeader("X_AUTH_TOKEN") String authToken) {
        return responseService.getSingleResponse(userService.getUserDetail(userRepository
                .findByUserId(jwtTokenProvider.getUserPk(authToken)).orElseThrow(CUserNotFoundException::new)));
    }

    @ApiOperation(value = "중복아이디 체크", notes = "아이디 입력")
    @GetMapping(value = "/user/check/{userId}")
    public SingleResponseDto<Boolean> checkId(@ApiParam(value = "회원ID", required = true) @PathVariable String userId) {

        return responseService.getSingleResponse(userRepository.existsByUserId(userId));
    }

    @ApiOperation(value = "Access토큰 재발급", notes = "refreshToken을 이용하여 accessToken 재발급")
    @PostMapping(value = "/tokenreissue")
    public SingleResponseDto tokenReissue(@RequestBody Map<String, Object> param) throws Exception {
        HashMap<String, Object> result = signService.tokenReissue((String) param.get("refreshToken"));
        return responseService.getSingleResponseDto(result.get("data"), Integer.parseInt(result.get("status").toString()),
                result.get("resultMsg").toString(), Boolean.valueOf((boolean) result.get("success")).booleanValue());
    }



}

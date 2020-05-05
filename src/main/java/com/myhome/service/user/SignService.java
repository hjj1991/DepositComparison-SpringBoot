package com.myhome.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.config.security.JwtTokenProvider;
import com.myhome.domain.token.TokenEntity;
import com.myhome.domain.token.TokenRepository;
import com.myhome.domain.user.Role;
import com.myhome.domain.user.UserEntity;
import com.myhome.domain.user.UserRepository;
import com.myhome.domain.user.dto.UserSignRequestDto;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SignService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("spring.jwt.secret")
    private String secretKey;

    @Transactional
    public void signUp(UserSignRequestDto userSignRequestDto) {
        userRepository.save(userSignRequestDto.toEntityWithPasswordEncode(passwordEncoder));
    }

    @Transactional
    public HashMap<String, String> signIn(UserEntity userEntity) throws Exception {

        System.out.println(userEntity.getUserId());

        HashMap<String, String> result = new HashMap<>();
        List<String> tokenInfo = new ArrayList<String>();
        tokenInfo = jwtTokenProvider.createToken(userEntity.getUserId(), userEntity.getRole());
        result.put("X_AUTH_TOKEN", tokenInfo.get(0));
        result.put("exAuthToken", tokenInfo.get(1));
        result.put("X_REFRESH_TOKEN", jwtTokenProvider.createRefreshToken(userEntity.getUserId(), userEntity.getRole()));
        result.put("userId", userEntity.getUserId());
        result.put("name", userEntity.getName());
        result.put("provider", userEntity.getProvider());
        result.put("picture", userEntity.getPicture());
        result.put("emailAddr", userEntity.getEmail());

        //로그인 시간 업데이트
        userEntity.update(userEntity.getName(), userEntity.getEmail(), userEntity.getPicture(), userEntity.getUserPw(), userEntity.getRole(), LocalDateTime.now());


        return result;
    }

    @Transactional
    public HashMap<String, String> signOut(String refreshToken) throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();
        System.out.println(refreshToken);
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {    //리프레쉬 토큰 검증 후 토큰 디코딩하여 정보가져옴
            String jwtUserId = Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(refreshToken).getBody().getSubject();
            System.out.println(jwtUserId);
            tokenRepository.deleteByUserId(jwtUserId);
            result.put("code", "0");
            result.put("msg", "로그아웃되었습니다.");
            result.put("success", "true");
        } else {
            result.put("code", "-1");
            result.put("msg", "잘못된 요청입니다.");
            result.put("success", "false");
        }

        return result;

//		result.put("X-AUTH-TOKEN", jwtTokenProvider.createToken(jwtUserId,jwtRoles)); //가져온 정보로 토큰 재생성
    }

    @Transactional
    public HashMap<String, Object> tokenReissue(String refreshToken) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> data = new HashMap<String, Object>();
        List<String> tokenInfo = new ArrayList<String>();
        if(jwtTokenProvider.validateRefreshToken(refreshToken)) {	//리프레쉬 토큰 검증 후 토큰 디코딩하여 정보가져옴
            TokenEntity tokenEntity = tokenRepository.findByRefreshToken(refreshToken);
            String jwtUserId = Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(refreshToken).getBody().getSubject();
            Role role = Role.valueOf(Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(refreshToken).getBody().get("role").toString().replace("ROLE_", ""));
            tokenInfo = jwtTokenProvider.createToken(jwtUserId, role);
            result.put("status", "0");
            data.put("X_AUTH_TOKEN", tokenInfo.get(0));
            data.put("exAuthToken", tokenInfo.get(1));
            result.put("data", data);
            result.put("resultMsg", "재발급되었습니다.");
            result.put("success", true);
        }else {
            result.put("status",  "-1");
            result.put("resultMsg", "잘못된 요청입니다.");
            result.put("success", false);
        }

        return result;
    }
}

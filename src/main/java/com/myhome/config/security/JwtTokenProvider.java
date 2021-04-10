package com.myhome.config.security;

import com.myhome.domain.token.TokenEntity;
import com.myhome.domain.token.TokenRepository;
import com.myhome.domain.user.Role;
import com.myhome.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    private final TokenRepository tokenRepository;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("spring.jwt.secret")
    private String secretKey;

    private long tokenValidMilisecond = 1000L * 60 * 20; // 20분만 토큰 유효
    private long refreshTokenValidMilisecond = 1000L * 60 * 1440 * 14; // 2주간 토큰 유효

    private final UserService userService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public List<String> createToken(String userPk, Role role) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("role", role.getKey());
        Date now = new Date();
        long expireTime = new Date().getTime() + tokenValidMilisecond;
        String token = Jwts.builder().setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(expireTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();

        List<String> result = new ArrayList<>();

        result.add(token);
        result.add(String.valueOf(expireTime));

        return result;

    }

    public String createRefreshToken(String userPk, Role role) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("role", role.getKey());
        Date now = new Date();
        LocalDateTime ldt = Instant.ofEpochMilli( now.getTime() )
                .atZone( ZoneId.systemDefault() )
                .toLocalDateTime();
        Date expiredDate = new Date(now.getTime() + refreshTokenValidMilisecond);

        String refreshToken = Jwts.builder().setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(expiredDate) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();


        if(tokenRepository.existsByUserId(userPk)) {
            TokenEntity updateTokenEntity = tokenRepository.findByUserId(userPk);
            Calendar cal = Calendar.getInstance();
            Date currentExpiredDate = Date.from( updateTokenEntity.getExpiredDatetime().atZone( ZoneId.systemDefault()).toInstant());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            cal.setTime(new Date());
            cal.add(Calendar.DATE, -2);
            System.out.println(df.format(currentExpiredDate).compareTo(df.format(cal.getTime())));
            if(df.format(currentExpiredDate).compareTo(df.format(cal.getTime())) < 0) {
                updateTokenEntity.update(refreshToken, LocalDateTime.ofInstant(expiredDate.toInstant(), ZoneId.systemDefault()));
            }else {
                refreshToken = updateTokenEntity.getRefreshToken();
            }
        }else {
            TokenEntity tokenEntity = TokenEntity.builder()
                    .userId(userPk)
                    .refreshToken(refreshToken)
                    .createdDatetime(ldt)
                    .expiredDatetime(LocalDateTime.ofInstant(expiredDate.toInstant(), ZoneId.systemDefault()))
                    .build();
            tokenRepository.save(tokenEntity);
        }

        return refreshToken;
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X_AUTH_TOKEN");
    }
    // Request의 Header에서 refreshToken 파싱 : "X-REFRESH-TOKEN: jwt토근"
    public String resolveRefreshToken(HttpServletRequest req) {
        return req.getHeader("X_REFRESH_TOKEN");
    }

    // Jwt 액세스토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            System.out.println(claims.getBody().getExpiration());
            System.out.println(new Date());
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    // Jwt 리프레쉬토큰의 유효성 + 만료일자 확인
    public boolean validateRefreshToken(String jwtToken) {
        if(tokenRepository.findByRefreshToken(jwtToken) != null) {
            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
                return !claims.getBody().getExpiration().before(new Date());
            } catch (Exception e) {
                return false;
            }
        }else {
            return false;
        }
    }
}
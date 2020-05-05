package com.myhome.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myhome.domain.user.Role;
import com.myhome.domain.user.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collections;

@Getter
@NoArgsConstructor
public class UserSignRequestDto {

    private String userId;
    @Email(message="잘못된 이메일주소입니다.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&*()_+|<>?:{}]{7,14}$", message ="비밀번호는 영문 숫자 조합 7 ~ 14자리 이상입니다.")
    private String userPw;
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message ="공백제외 한글, 영문, 숫자 2 ~ 10자로 입력해주세요.")
    private String name;
    private String picture;
    private String provider;
    private Role role;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiParam(hidden = true, required = false)
    private LocalDateTime loginDateTime;

    @Builder
    public UserSignRequestDto(String userId, String email, String userPw, String name, String picture, String provider, Role role, LocalDateTime loginDateTime){
        this.userId = userId;
        this.email = email;
        this.userPw = userPw;
        this.name = name;
        this.picture = picture;
        this.provider = provider;
        this.role = role;
        this.loginDateTime = loginDateTime;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(userId)
                .email(email)
                .picture(picture)
                .userPw(userPw)
                .name(name)
                .provider(provider)
                .role(role)
                .loginDateTime(loginDateTime)
                .build();
    }

    public UserEntity toEntityWithPasswordEncode(PasswordEncoder bCryptPasswordEncoder) {
        return UserEntity.builder()
                .userId(userId)
                .email(email)
                .userPw(bCryptPasswordEncoder.encode(userPw))
                .picture(picture)
                .name(name)
                .provider(provider)
                .role(role)
                .loginDateTime(loginDateTime)
                .build();
    }

    public UserEntity toEntityWithSocialSignIn() {
        return UserEntity.builder()
                .userId(userId)
                .email(email)
                .picture(picture)
                .name(name)
                .role(role)
                .build();
    }
}

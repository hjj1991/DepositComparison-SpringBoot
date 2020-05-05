package com.myhome.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myhome.domain.user.Role;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDetailResponseDto {
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String provider;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime loginDateTime;
}

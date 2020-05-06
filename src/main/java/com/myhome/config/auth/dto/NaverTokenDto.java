package com.myhome.config.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverTokenDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
}

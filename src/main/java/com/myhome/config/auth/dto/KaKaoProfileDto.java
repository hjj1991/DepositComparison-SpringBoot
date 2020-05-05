package com.myhome.config.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KaKaoProfileDto {
    private Long id;
    private Properties properties;

    @Getter
    @Setter
    @ToString
    public static class Properties {
        private String nickname;
        private String thumbnail_image;
        private String profile_image;
    }
}


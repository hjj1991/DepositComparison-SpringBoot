package com.myhome.config.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class NaverProfileDto {
    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private String id;
        private String nickname;
        private String name;
        private String email;
        private String gender;
        private String age;
        private String birthday;
        private String profile_image;
    }

}

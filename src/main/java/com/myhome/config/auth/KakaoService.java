package com.myhome.config.auth;

import com.myhome.config.auth.dto.KaKaoProfileDto;
import com.myhome.config.auth.dto.KaKaoTokenDto;
import com.myhome.domain.user.Role;
import com.myhome.domain.user.UserEntity;
import com.myhome.domain.user.UserRepository;
import com.myhome.domain.user.dto.UserSignRequestDto;
import com.myhome.service.user.SignService;
import com.myhome.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.h2.engine.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoService {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final SignService signService;


    private WebClient webClient = WebClient.builder()
//            .baseUrl("https://kauth.kakao.com/")
            .filter(logRequest())
            .build();

    // This method returns filter function which will log request data
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    public KaKaoProfileDto getKakaoProfile(String accessToken) {
        // Set parameter

        ClientResponse response = webClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Set header : Content-type: application/x-www-form-urlencoded
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .block();


        if (response.statusCode().is2xxSuccessful()) {
//            System.out.println(response.bodyToMono(String.class).block());
            return response.bodyToMono(KaKaoProfileDto.class).block();

        }

        return null;

    }



    public HashMap<String, String> getKakaoTokenInfo(String code) throws Exception {

        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "656c5afa5455de8f5ad9eb51e09e3720");
        params.add("redirect_uri", "http://localhost:3000/sociallogin?provider=kakao");
        params.add("code", code);


        ClientResponse response = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Set header : Content-type: application/x-www-form-urlencoded
                .body(BodyInserters.fromFormData(params))
                .exchange()
                .block();

        if(response.statusCode().is2xxSuccessful()){
            KaKaoProfileDto kaKaoProfileDto = getKakaoProfile(response.bodyToMono(KaKaoTokenDto.class).block().getAccess_token());

            System.out.println(kaKaoProfileDto.getProperties().toString());

            if(kaKaoProfileDto != null){
                Optional user = userRepository.findByUserId(kaKaoProfileDto.getId().toString());
                if(user.isPresent()){
                    UserEntity userEntity = (UserEntity) user.get();
                    userEntity.update(kaKaoProfileDto.getProperties().getNickname(),
                            userEntity.getEmail(),
                            kaKaoProfileDto.getProperties().getThumbnail_image(),
                            null,
                            userEntity.getRole(),
                            LocalDateTime.now());
                    return signService.signIn(userEntity);

                }else{
                    UserEntity logedUser = userRepository.save(
                            UserSignRequestDto.builder()
                                    .userId(kaKaoProfileDto.getId().toString())
                                    .name(kaKaoProfileDto.getProperties().getNickname())
                                    .role(Role.GUEST)
                                    .provider("KAKO")
                                    .picture(kaKaoProfileDto.getProperties().getThumbnail_image())
                                    .build().toEntity());
                    return signService.signIn(logedUser);

                }


            }
        }
        return null;


    }
}

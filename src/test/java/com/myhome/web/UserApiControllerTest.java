package com.myhome.web;

import com.myhome.config.auth.dto.KaKaoTokenDto;
import com.myhome.domain.user.Role;
import com.myhome.domain.user.UserEntity;
import com.myhome.domain.user.UserRepository;
import com.myhome.domain.user.dto.UserSignRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void 등록한다() throws Exception {
        //given
        String email = "hjj19911@naver.com";
        String name = "황재정";
        UserSignRequestDto requestDto = UserSignRequestDto.builder()
                .email(email)
                .name(name)
                .userPw("dkagh2383!@#")
                .role(Role.GUEST)
                .build();

        String url = "http://localhost:" + port + "/v1/signup";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<UserEntity> all = userRepository.findAll();
        Assertions.assertThat(all.get(0).getEmail()).isEqualTo(email);
        Assertions.assertThat(all.get(0).getName()).isEqualTo(name);
    }

    @Test
    public void Role_테스트() throws Exception {
        System.out.println(Role.valueOf("USER"));
        System.out.println(Role.valueOf("ROLE_USER".replace("ROLE_", "")));
    }
    @Test
    public void 카카오리턴값테스트() throws Exception {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com/")
                .build();

        String code = "idakS2Ahl8A17lE6gaxtBzV3hOf9NJQC0hwjGp2CEjJkL9kpZevgdgQWg0ZWvkKaacUgjQopcBQAAAFx4Odq1w";
        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "656c5afa5455de8f5ad9eb51e09e3720");
        params.add("redirect_uri", "http://localhost:8080/v1/social/kakao/login");
        params.add("code", code);


        ClientResponse response = webClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Set header : Content-type: application/x-www-form-urlencoded
                .body(BodyInserters.fromFormData(params))
                .exchange()
                .block();

        System.out.println(response.statusCode());
        System.out.println(response.bodyToMono(KaKaoTokenDto.class).block());

    }


}

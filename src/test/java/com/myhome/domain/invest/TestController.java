package com.myhome.domain.invest;

import com.myhome.domain.invest.dto.InstallmentSavingDto;
import com.myhome.domain.invest.service.InstallmentSavingService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {

    @LocalServerPort
    private int port;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    InstallmentSavingService installmentSavingService;

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



    @Test
    public void 적금호출해본다() throws Exception{
        int pageNo = 1;
        ModelMapper modelMapper = new ModelMapper();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("auth", "37cc4a0dc642205bb06d557239e72e77");
        params.add("topFinGrpNo", "020000");
        params.add("pageNo", String.valueOf(pageNo));
        while(true){
            ClientResponse response = webClient.get()
                    .uri((uriBuilder) -> uriBuilder.scheme("http")

                            .host("finlife.fss.or.kr")
                            .path("finlifeapi/savingProductsSearch.json")
                            .queryParams(params)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .block();

            InstallmentSavingDto installmentSavingDto = response.bodyToMono(InstallmentSavingDto.class).block();
//            InstallmentSavingEntity installmentSavingEntity = modelMapper.map(installmentSavingDto.getResult().getBaseList().get(0), InstallmentSavingEntity.class);
//            installmentSavingEntity = modelMapper.map(installmentSavingDto.getResult().getOptionlist().get(0), InstallmentSavingEntity.class);
            System.out.println(installmentSavingDto.getResult().getOptionList().get(0).getFinCoNo());
//            Assert.assertEquals(installmentSavingDto.getResult().getBaseList().get(0).getDclsMonth(), installmentSavingEntity.getDclsMonth());

            if(pageNo < Integer.valueOf(installmentSavingDto.getResult().getMaxPageNo())){
                pageNo++;
            }else{
                break;
            }
        }
    }
}

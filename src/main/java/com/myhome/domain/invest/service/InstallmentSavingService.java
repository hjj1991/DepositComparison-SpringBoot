package com.myhome.domain.invest.service;

import com.myhome.domain.invest.InstallmentSavingEntity;
import com.myhome.domain.invest.InstallmentSavingOptionEntity;
import com.myhome.domain.invest.InstallmentSavingRepository;
import com.myhome.domain.invest.dto.InstallmentSavingDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InstallmentSavingService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final InstallmentSavingRepository installmentSavingRepository;

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

    @Transactional
    public List<InstallmentSavingEntity> getInstallmentSavingList() {
        int pageNo = 1;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("auth", "37cc4a0dc642205bb06d557239e72e77");
        params.add("topFinGrpNo", "020000");
        params.add("pageNo", String.valueOf(pageNo));
        List<InstallmentSavingEntity> result = new ArrayList<InstallmentSavingEntity>();
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
            List<InstallmentSavingEntity> installmentSavingEntityList = installmentSavingDto.getResult().getBaseList().stream().map(new Function<InstallmentSavingDto.Baselist, InstallmentSavingEntity>() {
                @Override
                public InstallmentSavingEntity apply(InstallmentSavingDto.Baselist baselist) {
                    InstallmentSavingEntity tempEntity = new InstallmentSavingEntity();
                    tempEntity = modelMapper.map(baselist, InstallmentSavingEntity.class);
                    return tempEntity;
                }
            }).collect(Collectors.toList());
            List<InstallmentSavingOptionEntity> installmentSavingOptionEntityList = installmentSavingDto.getResult().getOptionList().stream().map(new Function<InstallmentSavingDto.Optionlist, InstallmentSavingOptionEntity>() {
                @Override
                public InstallmentSavingOptionEntity apply(InstallmentSavingDto.Optionlist optionlist){
                    InstallmentSavingOptionEntity tempEntity = new InstallmentSavingOptionEntity();
                    tempEntity = modelMapper.map(optionlist, InstallmentSavingOptionEntity.class);
                    return tempEntity;
                }
            }).collect(Collectors.toList());
            for (InstallmentSavingEntity installmentSavingEntity: installmentSavingEntityList) {
                List<InstallmentSavingOptionEntity> optionEntities = new ArrayList<InstallmentSavingOptionEntity>();
                for (InstallmentSavingOptionEntity installmentSavingOptionEntity: installmentSavingOptionEntityList) {

                    if(installmentSavingOptionEntity.getFinPrdtCd().equals(installmentSavingEntity.getFinPrdtCd())){
                        optionEntities.add(installmentSavingOptionEntity);
                    }
                }
                installmentSavingEntity.setOptions(optionEntities);
            }

            result.addAll(installmentSavingEntityList);
//            installmentSavingRepository.saveAll(installmentSavingEntityList);

            if(pageNo < Integer.valueOf(installmentSavingDto.getResult().getMaxPageNo())){
                pageNo++;
            }else{
                break;
            }
        }
        return result;

    }

}

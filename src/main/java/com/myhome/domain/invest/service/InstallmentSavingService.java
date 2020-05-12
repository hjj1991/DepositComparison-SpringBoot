package com.myhome.domain.invest.service;

import com.myhome.domain.invest.*;
import com.myhome.domain.invest.dto.InstallmentResponseDto;
import com.myhome.domain.invest.dto.InstallmentSavingDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final InstallmentSavingOptionRepository installmentSavingOptionRepository;
    @Autowired
    private BankRepository bankRepository;

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

//    @Transactional(readOnly = true)
//    public Page<InstallmentSavingEntity> findInstallmentSaving(PageRequest pageRequest) {
//        Pageable pageRequest1 = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),  pageRequest.getPageSize());
//        return installmentSavingRepository.findAll(pageRequest1);
//    }
    public List<InstallmentResponseDto> findInstallmentSaving() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<InstallmentSavingEntity> installmentSavingEntityList = installmentSavingRepository.findAllJoinFetch();
        List<InstallmentResponseDto> installmentResponseDtoList = installmentSavingEntityList.stream().map(new Function<InstallmentSavingEntity, InstallmentResponseDto>() {
            @Override
            public InstallmentResponseDto apply(InstallmentSavingEntity installmentSavingEntity) {
                InstallmentResponseDto installmentResponseDto = modelMapper.map(installmentSavingEntity, InstallmentResponseDto.class);
                System.out.println(installmentSavingEntity.getBankInfo().getBankRole());
                installmentResponseDto.setBankInfo(modelMapper.map(installmentSavingEntity.getBankInfo(), InstallmentResponseDto.BankInfo.class));
                installmentResponseDto.setOptionList(installmentSavingEntity.getOptions().stream().map(new Function<InstallmentSavingOptionEntity, InstallmentResponseDto.Options>() {
                    @Override
                    public InstallmentResponseDto.Options apply(InstallmentSavingOptionEntity installmentSavingOptionEntity){
                        InstallmentResponseDto.Options options = modelMapper.map(installmentSavingOptionEntity, InstallmentResponseDto.Options.class);
                        return options;
                    }
                }).collect(Collectors.toList()));
                return installmentResponseDto;
            }
        }).collect(Collectors.toList());
        return installmentResponseDtoList;
    }

    @Transactional
    public void getInstallmentSavingList(String topFinGrpNo) {
        int pageNo = 1;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("auth", "37cc4a0dc642205bb06d557239e72e77");
        params.add("topFinGrpNo", topFinGrpNo);
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
            List<InstallmentSavingEntity> installmentSavingEntityList = installmentSavingDto.getResult().getBaseList().stream().map(new Function<InstallmentSavingDto.Baselist, InstallmentSavingEntity>() {
                @Override
                public InstallmentSavingEntity apply(InstallmentSavingDto.Baselist baselist) {
                    InstallmentSavingEntity tempEntity = new InstallmentSavingEntity();
                    tempEntity = modelMapper.map(baselist, InstallmentSavingEntity.class);
                    String finPrdtCd = tempEntity.getFinPrdtCd();
                    List<InstallmentSavingOptionEntity> installmentSavingOptionEntityList = installmentSavingDto.getResult().getOptionList().stream().map(new Function<InstallmentSavingDto.Optionlist, InstallmentSavingOptionEntity>() {
                        @Override
                        public InstallmentSavingOptionEntity apply(InstallmentSavingDto.Optionlist optionlist){
                            InstallmentSavingOptionEntity tempEntity2 = new InstallmentSavingOptionEntity();
                            tempEntity2 = modelMapper.map(optionlist, InstallmentSavingOptionEntity.class);


                            return tempEntity2;
                        }
                    }).filter(t -> t.getFinPrdtCd().equals(finPrdtCd)).collect(Collectors.toList());
                    List<InstallmentSavingOptionEntity> resultList = new ArrayList<>();
//                    for(InstallmentSavingOptionEntity installmentSavingOptionEntity: installmentSavingOptionEntityList){
//                        if(installmentSavingOptionEntity.getFinPrdtCd().equals(finPrdtCd)){
//                            resultList.add(installmentSavingOptionEntity);
//                        }
//                    }
                    BankEntity bankEntity = bankRepository.findFirstByFinCoNo(baselist.getFinCoNo());
                    if(bankEntity == null){

                    }else {
                        tempEntity.update(bankRepository.findFirstByFinCoNo(baselist.getFinCoNo()), installmentSavingOptionEntityList);
                    }
//                    System.out.println(tempEntity.toString());
//                    System.out.println("호이호이호이");
//                    System.out.println(installmentSavingOptionEntityList.size());
                    for(InstallmentSavingOptionEntity installmentSavingOptionEntity: installmentSavingOptionEntityList){
//                        System.out.println(installmentSavingOptionEntity.toString());
                    }
//                    installmentSavingRepository.save(tempEntity);
                    return tempEntity;
                }
            }).collect(Collectors.toList());

            installmentSavingRepository.saveAll(installmentSavingEntityList);

            if(pageNo < Integer.valueOf(installmentSavingDto.getResult().getMaxPageNo())){
                pageNo++;
            }else{
                break;
            }
        }

    }

}
package com.myhome.domain.invest.service;

import com.myhome.domain.invest.*;
import com.myhome.domain.invest.dto.DepositDto;
import com.myhome.domain.invest.dto.DepositResponseDto;
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
public class DepositService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BankRepository bankRepository;

    private final DepositRepository depositRepository;

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

    public List<DepositResponseDto> findDeposit() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<DepositEntity> depositEntityList = depositRepository.findAllJoinFetch();
        List<DepositResponseDto> depositResponseDtoList = depositEntityList.stream().map(new Function<DepositEntity, DepositResponseDto>() {
            @Override
            public DepositResponseDto apply(DepositEntity depositEntity) {
                DepositResponseDto depositResponseDto = modelMapper.map(depositEntity, DepositResponseDto.class);
                depositResponseDto.setBankInfo(modelMapper.map(depositEntity.getBankInfo(), DepositResponseDto.BankInfo.class));
                depositResponseDto.setOptionList(depositEntity.getOptions().stream().map(new Function<DepositOptionEntity, DepositResponseDto.Options>() {
                    @Override
                    public DepositResponseDto.Options apply(DepositOptionEntity depositOptionEntity){
                        DepositResponseDto.Options options = modelMapper.map(depositOptionEntity, DepositResponseDto.Options.class);
                        return options;
                    }
                }).collect(Collectors.toList()));
                return depositResponseDto;
            }
        }).collect(Collectors.toList());
        return depositResponseDtoList;
    }



    @Transactional
    public void getDepositList(String topFinGrpNo) {
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
                            .path("finlifeapi/depositProductsSearch.json")
                            .queryParams(params)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .block();

            DepositDto depositDto = response.bodyToMono(DepositDto.class).block();
            List<DepositEntity> depositEntityList = depositDto.getResult().getBaseList().stream().map(new Function<DepositDto.Baselist, DepositEntity>() {
                @Override
                public DepositEntity apply(DepositDto.Baselist baselist) {
                    DepositEntity tempEntity = depositRepository.findTopByFinPrdtCdAndFinCoNo(baselist.getFinPrdtCd(), baselist.getFinCoNo());
                    if(tempEntity == null) {
                        tempEntity = modelMapper.map(baselist, DepositEntity.class);
                    }else{
                        tempEntity = tempEntity.update(baselist.getMaxLimit(), baselist.getSpclCnd(), baselist.getMtrtInt(), baselist.getJoinMember(), baselist.getJoinWay(),
                                baselist.getJoinDeny(), baselist.getKorCoNm(), baselist.getFinCoNo(), baselist.getFinPrdtNm(), baselist.getEtcNote(), baselist.getDclsMonth(),
                                baselist.getFinCoSubmDay());
                    }
                    String finPrdtCd = tempEntity.getFinPrdtCd();
                    String finCoNm = tempEntity.getFinCoNo();
                    List<DepositOptionEntity> depositOptionEntityList = depositDto.getResult().getOptionList().stream().map(new Function<DepositDto.Optionlist, DepositOptionEntity>() {
                        @Override
                        public DepositOptionEntity apply(DepositDto.Optionlist optionlist){
                            DepositOptionEntity tempEntity2 = new DepositOptionEntity();
                            tempEntity2 = modelMapper.map(optionlist, DepositOptionEntity.class);


                            return tempEntity2;
                        }
                    }).filter(t -> t.getFinPrdtCd().equals(finPrdtCd) && t.getFinCoNo().equals(finCoNm)).collect(Collectors.toList());
                    List<DepositOptionEntity> resultList = new ArrayList<>();
                    BankEntity bankEntity = bankRepository.findFirstByFinCoNo(baselist.getFinCoNo());
                    if(bankEntity == null){

                    }else {
                        tempEntity.update(bankRepository.findFirstByFinCoNo(baselist.getFinCoNo()), depositOptionEntityList);
                    }
                    return tempEntity;
                }
            }).collect(Collectors.toList());

            depositRepository.saveAll(depositEntityList);

            if(pageNo < Integer.valueOf(depositDto.getResult().getMaxPageNo())){
                pageNo++;
            }else{
                break;
            }
        }

    }
}

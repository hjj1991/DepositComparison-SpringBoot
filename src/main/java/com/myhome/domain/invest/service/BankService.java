package com.myhome.domain.invest.service;

import com.myhome.domain.invest.*;
import com.myhome.domain.invest.dto.BankDto;
import com.myhome.domain.invest.dto.InstallmentSavingDto;
import lombok.NoArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class BankService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final BankRepository bankRepository;
    private final BankBranchRepository bankBranchRepository;

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



    public void getBankList(String topFinGrpNo) {
        int pageNo = 1;

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("auth", "37cc4a0dc642205bb06d557239e72e77");
        params.add("topFinGrpNo", topFinGrpNo);
        params.add("pageNo", String.valueOf(pageNo));
        List<BankEntity> result = new ArrayList<BankEntity>();
        while(true){
            ClientResponse response = webClient.get()
                    .uri((uriBuilder) -> uriBuilder.scheme("http")

                            .host("finlife.fss.or.kr")
                            .path("finlifeapi/companySearch.json")
                            .queryParams(params)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .block();

            BankDto bankDto = response.bodyToMono(BankDto.class).block();
            List<BankEntity> bankEntityList = bankDto.getResult().getBaseList().stream().map(new Function<BankDto.Baselist, BankEntity>() {
                @Override
                public BankEntity apply(BankDto.Baselist baselist) {
                    String bankRole;
                    if(topFinGrpNo.equals("020000")) {
                        bankRole = "은행";
                    }else if(topFinGrpNo.equals("030300")) {
                        bankRole = "저축은행";
                    }else {
                        bankRole = "없음";
                    }
                    BankEntity tempEntity = new BankEntity();
                    tempEntity = modelMapper.map(baselist, BankEntity.class);
                    String finCoNo = tempEntity.getFinCoNo();
                    List<BankBranchEntity> resultList = new ArrayList<>();
                    List<BankBranchEntity> bankBranchEntityList = bankDto.getResult().getOptionList().stream().map(new Function<BankDto.Optionlist, BankBranchEntity>() {
                        @Override
                        public BankBranchEntity apply(BankDto.Optionlist optionlist) {
                            BankBranchEntity tempEntity2 = new BankBranchEntity();
                            tempEntity2 = modelMapper.map(optionlist, BankBranchEntity.class);
                            return tempEntity2;
                        }
                    }).filter(t -> t.getFinCoNo().equals(finCoNo)).collect(Collectors.toList());
//                    for(BankBranchEntity bankBranchEntity: bankBranchEntityList){
//                        if(bankBranchEntity.getFinCoNo().equals(finCoNo))
//                            resultList.add(bankBranchEntity);
//                    }
                    tempEntity.update(bankRole, bankBranchEntityList);
//                    tempEntity.setBankBranchList(bankBranchEntityList);


                    return tempEntity;
                }
            }).collect(Collectors.toList());



            bankRepository.saveAll(bankEntityList);

            if(pageNo < Integer.valueOf(bankDto.getResult().getMaxPageNo())){
                pageNo++;
            }else{
                break;
            }
        }

    }

}
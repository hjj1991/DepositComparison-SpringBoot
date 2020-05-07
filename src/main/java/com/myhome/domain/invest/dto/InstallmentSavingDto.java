package com.myhome.domain.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.domain.invest.InstallmentSavingEntity;
import com.myhome.domain.invest.InstallmentSavingOptionEntity;
import com.myhome.domain.invest.InstallmentSavingRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class InstallmentSavingDto {

    private Result result;
    @Autowired
    InstallmentSavingRepository installmentSavingRepository;

    @Getter
    @Setter
    public static class Result {
        private List<Optionlist> optionList;
        private List<Baselist> baseList;
        @JsonProperty("err_msg")
        private String errMsg;
        @JsonProperty("err_cd")
        private String errCd;
        @JsonProperty("now_page_no")
        private String nowPageNo;
        @JsonProperty("max_page_no")
        private String maxPageNo;
        @JsonProperty("total_count")
        private String totalCount;
        @JsonProperty("prdt_div")
        private String prdtDiv;
    }

    @Getter
    @Setter
    public static class Optionlist {
        @JsonProperty("intr_rate2")
        private double intrRate2;
        @JsonProperty("intr_rate")
        private double intrRate;
        @JsonProperty("save_trm")
        private String saveTrm;
        @JsonProperty("rsrv_type_nm")
        private String rsrvTypeNm;
        @JsonProperty("rsrv_type")
        private String rsrvType;
        @JsonProperty("intr_rate_type_nm")
        private String intrRateTypeNm;
        @JsonProperty("intr_rate_type")
        private String intrRateType;
        @JsonProperty("fin_prdt_cd")
        private String finPrdtCd;
        @JsonProperty("fin_co_no")
        private String finCoNo;
        @JsonProperty("dcls_month")
        private String dclsMonth;
    }

    @Getter
    @Setter
    public static class Baselist {
        @JsonProperty("fin_co_subm_day")
        private String finCoSubmDay;
        @JsonProperty("dcls_strt_day")
        private String dclsStrtDay;
        @JsonProperty("max_limit")
        private long maxLimit;
        @JsonProperty("etc_note")
        private String etcNote;
        @JsonProperty("join_member")
        private String joinMember;
        @JsonProperty("join_deny")
        private String joinDeny;
        @JsonProperty("spcl_cnd")
        private String spclCnd;
        @JsonProperty("mtrt_int")
        private String mtrtInt;
        @JsonProperty("join_way")
        private String joinWay;
        @JsonProperty("fin_prdt_nm")
        private String finPrdtNm;
        @JsonProperty("kor_co_nm")
        private String korCoNm;
        @JsonProperty("fin_prdt_cd")
        private String finPrdtCd;
        @JsonProperty("fin_co_no")
        private String finCoNo;
        @JsonProperty("dcls_month")
        private String dclsMonth;
    }

    public InstallmentSavingEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return InstallmentSavingEntity.builder()
                .dclsMonth(result.getBaseList().get(0).dclsMonth)
                .etcNote(result.getBaseList().get(0).etcNote)
                .finCoNo(result.getBaseList().get(0).finCoNo)
                .finCoSubmDay(result.getBaseList().get(0).finCoSubmDay)
                .finPrdtCd(result.getBaseList().get(0).finPrdtCd)
                .finPrdtNm(result.getBaseList().get(0).finPrdtNm)
                .joinDeny(result.getBaseList().get(0).joinDeny)
                .joinMember(result.getBaseList().get(0).joinMember)
                .joinWay(result.getBaseList().get(0).joinWay)
                .korCoNm(result.getBaseList().get(0).korCoNm)
                .maxLimit(result.getBaseList().get(0).maxLimit)
                .mtrtInt(result.getBaseList().get(0).mtrtInt)
                .spclCnd(result.getBaseList().get(0).spclCnd)
                .options(result.getOptionList().stream().map(new Function<Optionlist, InstallmentSavingOptionEntity>() {

                    @Override
                    public InstallmentSavingOptionEntity apply(Optionlist t) {
                        InstallmentSavingOptionEntity tempEntity = new InstallmentSavingOptionEntity();
                        System.out.println(t.getIntrRateType());
                        tempEntity = modelMapper.map(t, InstallmentSavingOptionEntity.class);
                        return tempEntity;
                    }
                }).collect(Collectors.toList()))
                .build();

    }
}

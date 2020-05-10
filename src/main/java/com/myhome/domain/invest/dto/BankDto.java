package com.myhome.domain.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BankDto {


    private Result result;

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
        @JsonProperty("exis_yn")
        private String exisYn;
        @JsonProperty("area_nm")
        private String areaNm;
        @JsonProperty("area_cd")
        private String areaCd;
        @JsonProperty("fin_co_no")
        private String finCoNo;
        @JsonProperty("dcls_month")
        private String dclsMonth;
    }

    @Getter
    @Setter
    public static class Baselist {
        @JsonProperty("cal_tel")
        private String calTel;
        @JsonProperty("homp_url")
        private String hompUrl;
        @JsonProperty("dcls_chrg_man")
        private String dclsChrgMan;
        @JsonProperty("kor_co_nm")
        private String korCoNm;
        @JsonProperty("fin_co_no")
        private String finCoNo;
        @JsonProperty("dcls_month")
        private String dclsMonth;
    }
}

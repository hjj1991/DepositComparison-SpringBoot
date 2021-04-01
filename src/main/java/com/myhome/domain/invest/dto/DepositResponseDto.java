package com.myhome.domain.invest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DepositResponseDto {

    private String spclCnd;
    private List<DepositResponseDto.Options> optionList = new ArrayList<>();
    private String mtrtInt;
    private Long maxLimit;
    private String korCoNm;
    private String joinWay;
    private String joinMember;
    private String joinDeny;
    private Long id;
    private String finPrdtNm;
    private String finPrdtCd;
    private String finCoSubmDay;
    private String finCoNo;
    private String etcNote;
    private String dclsMonth;
    private DepositResponseDto.BankInfo bankInfo;

    @Getter
    @Setter
    public static class Options {
        private String saveTrm;
        private String intrRateTypeNm;
        private String intrRateType;
        private double intrRate2;
        private double intrRate;
        private Long id;
        private String finPrdtCd;
        private String finCoNo;
        private String dclsMonth;
    }

    @Getter
    @Setter
    public static class BankInfo {
        private String korCoNm;
        private String hompUrl;
        private String finCoNo;
        private String dclsMonth;
        private String dclsChrgMan;
        private String calTel;
        private String bankRole;
        private List<DepositResponseDto.BankBranchList> bankBranchList = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class BankBranchList {
        private Long id;
        private String finCoNo;
        private String exisYn;
        private String dclsMonth;
        private String areaNm;
        private String areaCd;
    }
}

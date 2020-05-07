package com.myhome.domain.invest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "tbl_installment_saving_option")
@Entity
public class InstallmentSavingOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column //저축 금리 [소수점 2자리]
    private double intrRate2;
    @Column //최고 우대금리[소수점 2자리]
    private double intrRate;
    @Column //저축 기간[단위: 개월]
    private String saveTrm;
    @Column //적립 유형명
    private String rsrvTypeNm;
    @Column //적립 유형
    private String rsrvType;
    @Column //저축 금리 유형명
    private String intrRateTypeNm;
    @Column //저축 금리 유형
    private String intrRateType;
    @Column
    private String finPrdtCd;
    @Column //금융회사코드
    private String finCoNo;
    @Column //금융상품코드
    private String dclsMonth;




}

package com.myhome.domain.invest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Table(name = "tbl_bank")
@Entity
public class BankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String calTel;
    @Column
    private String hompUrl;
    @Column
    private String dclsChrgMan;
    @Column
    private String korCoNm;
    @Column
    private String finCoNo;
    @Column
    private String dclsMonth;

    @Column
    private String bankRole;

    @Column
    private LocalDateTime updateDateTime = LocalDateTime.now();
    
    /*
        1: 삭제
        0: 동기화완료
        9: 동기화중
     */
    @Column
    private int deleteFlag;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="bankEntity_id")
    private List<BankBranchEntity> bankBranchList = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankInfo")
    private List<InstallmentSavingEntity> installmentSavingEntityList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankInfo")
    private List<DepositEntity> depositEntityList = new ArrayList<>();

    @Builder
    private BankEntity(String calTel, String hompUrl, String dclsChrgMan, String korCoNm, String finCoNo, String dclsMonth, String bankRole){
        this.calTel = calTel;
        this.hompUrl = hompUrl;
        this.dclsChrgMan = dclsChrgMan;
        this.korCoNm = korCoNm;
        this.finCoNo = finCoNo;
        this.dclsMonth = dclsMonth;
        this.bankRole = bankRole;
        this.updateDateTime = LocalDateTime.now();
        this.deleteFlag = 0;
//        this.bankBranchList = bankBranchEntityList;
//        this.installmentSavingEntityList = installmentSavingEntityList;
    }

    public BankEntity update(String calTel, String hompUrl, String dclsChrgMan, String korCoNm, String dclsMonth, String bankRole){
        this.calTel = calTel;
        this.hompUrl = hompUrl;
        this.dclsChrgMan = dclsChrgMan;
        this.korCoNm = korCoNm;
        this.dclsMonth = dclsMonth;
        this.bankRole = bankRole;
        this.updateDateTime = LocalDateTime.now();
        this.deleteFlag = 0;
        return this;
//        this.bankBranchList = bankBranchEntityList;
//        this.installmentSavingEntityList = installmentSavingEntityList;
    }

    public BankEntity update(String bankRole, List<BankBranchEntity> bankBranchEntityList){
        this.bankRole = bankRole;
        this.bankBranchList = bankBranchEntityList;
        return this;
    }






}

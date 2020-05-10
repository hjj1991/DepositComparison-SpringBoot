package com.myhome.domain.invest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Table(name = "tbl_bank")
@Entity
@Setter
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="bankEntity_id")
    private List<BankBranchEntity> bankBranchList = new ArrayList<>();


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankInfo")
    private List<InstallmentSavingEntity> installmentSavingEntityList = new ArrayList<>();

    @Builder
    private BankEntity(String calTel, String hompUrl, String dclsChrgMan, String korCoNm, String finCoNo, String dclsMonth, String bankRole){
        this.calTel = calTel;
        this.hompUrl = hompUrl;
        this.dclsChrgMan = dclsChrgMan;
        this.korCoNm = korCoNm;
        this.finCoNo = finCoNo;
        this.dclsMonth = dclsMonth;
        this.bankRole = bankRole;
//        this.bankBranchList = bankBranchEntityList;
//        this.installmentSavingEntityList = installmentSavingEntityList;
    }

    public BankEntity update(String bankRole, List<BankBranchEntity> bankBranchEntityList){
        this.bankRole = bankRole;
        this.bankBranchList = bankBranchEntityList;
        return this;
    }






}

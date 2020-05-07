package com.myhome.domain.invest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_installment_saving")
@Entity
public class InstallmentSavingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column//최고한도
    private long maxLimit;
    @Column(columnDefinition = "varchar(2000)")//우대조건
    private String spclCnd;
    @Column(columnDefinition = "varchar(2000)")//만기 후 이자율
    private String mtrtInt;
    @Column//가입대상
    private String joinMember;
    @Column//가입방법
    private String joinWay;
    @Column//가입제한 EX) 1:제한없음, 2:서민전용, 3일부제한
    private String joinDeny;
    @Column//금융회사명
    private String korCoNm;
    @Column//금융회사코드
    private String finCoNo;
    @Column//금융상품코드
    private String finPrdtCd;
    @Column//금융상품명
    private String finPrdtNm;
    @Column//기타 유의사항
    private String etcNote;
    @Column//공시 제출일[YYYYMM]
    private String dclsMonth;
    @Column//금융회사 제출일 [YYYYMMDDHH24MI]
    private String finCoSubmDay;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="installmentSavingOptionEntity_finPrdtCd")
    private List<InstallmentSavingOptionEntity> options;

    @Builder
    private InstallmentSavingEntity(long maxLimit, String spclCnd, String mtrtInt,
                                    String joinMember, String joinWay, String joinDeny,
                                    String korCoNm, String finCoNo, String finPrdtCd,
                                    String finPrdtNm, String etcNote, String dclsMonth, String finCoSubmDay, List<InstallmentSavingOptionEntity> options) {
        this.maxLimit = maxLimit;
        this.spclCnd = spclCnd;
        this.mtrtInt = mtrtInt;
        this.joinMember = joinMember;
        this.joinWay = joinWay;
        this.joinDeny = joinDeny;
        this.korCoNm = korCoNm;
        this.finCoNo = finCoNo;
        this.finPrdtCd = finPrdtCd;
        this.finPrdtNm = finPrdtNm;
        this.etcNote = etcNote;
        this.dclsMonth = dclsMonth;
        this.finCoSubmDay = finCoSubmDay;
        this.options = options;
    }
}

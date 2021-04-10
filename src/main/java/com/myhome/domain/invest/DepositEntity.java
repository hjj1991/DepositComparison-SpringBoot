package com.myhome.domain.invest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "tbl_deposit")
@Entity
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column//최고한도
    private Long maxLimit;
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

    @Column
    private LocalDateTime updateDateTime = LocalDateTime.now();

    /*
        1: 삭제
        0: 동기화완료
        9: 동기화중
     */
    @Column
    private int deleteFlag;



    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="depositEntity_id")
    private List<DepositOptionEntity> options;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depositEntity")
    private List<DepositCommentEntity> comments;

    @ManyToOne
    @JoinColumn(name="bankEntity_id", nullable = true)
    private BankEntity bankInfo;

    @Builder
    private DepositEntity(long maxLimit, String spclCnd, String mtrtInt,
                                    String joinMember, String joinWay, String joinDeny,
                                    String korCoNm, String finCoNo, String finPrdtCd,
                                    String finPrdtNm, String etcNote, String dclsMonth, String finCoSubmDay,
                          List<DepositOptionEntity> options,  BankEntity bankInfo) {
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
        this.bankInfo = bankInfo;
        this.updateDateTime = LocalDateTime.now();
        this.deleteFlag = 0;
    }

    public DepositEntity update(long maxLimit, String spclCnd, String mtrtInt,
                                String joinMember, String joinWay, String joinDeny,
                                String korCoNm, String finCoNo,
                                String finPrdtNm, String etcNote, String dclsMonth, String finCoSubmDay) {
        this.maxLimit = maxLimit;
        this.spclCnd = spclCnd;
        this.mtrtInt = mtrtInt;
        this.joinMember = joinMember;
        this.joinWay = joinWay;
        this.joinDeny = joinDeny;
        this.korCoNm = korCoNm;
        this.finCoNo = finCoNo;
        this.finPrdtNm = finPrdtNm;
        this.etcNote = etcNote;
        this.dclsMonth = dclsMonth;
        this.finCoSubmDay = finCoSubmDay;
        this.updateDateTime = LocalDateTime.now();
        this.deleteFlag = 0;
        return this;
    }

    public DepositEntity update(BankEntity bankInfo, List<DepositOptionEntity> options){
        this.bankInfo = bankInfo;
        this.options = options;
        return this;
    }
    public DepositEntity update(BankEntity bankInfo){
        this.bankInfo = bankInfo;
        return this;
    }
}

package com.myhome.domain.invest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "tbl_bank_Branch")
@Entity
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class BankBranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String exisYn;
    @Column
    private String areaNm;
    @Column
    private String areaCd;
    @Column
    private String finCoNo;
    @Column
    private String dclsMonth;

    @Builder
    public BankBranchEntity(String exisYn, String areaNm, String areaCd, String finCoNo, String dclsMonth){
        this.exisYn = exisYn;
        this.areaNm = areaNm;
        this.areaCd = areaCd;
        this.finCoNo = finCoNo;
        this.dclsMonth = dclsMonth;
    }

}

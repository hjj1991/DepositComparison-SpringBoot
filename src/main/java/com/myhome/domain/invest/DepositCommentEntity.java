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
@Table(name = "tbl_deposit_comment")
@Entity
public class DepositCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String contents;

    @Column(nullable = false, columnDefinition = "varchar(1) default 'N'")
    private String deletedYn;

    @Column(nullable = false)
    private String creatorId;

    @Column(nullable = false)
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column
    private LocalDateTime updatedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depositEntity_id")
    private DepositEntity depositEntity;

    @Builder
    private DepositCommentEntity(String contents, String deletedYn, String creatorId, DepositEntity depositEntity){
        this.contents = contents;
        this.deletedYn = deletedYn;
        this.creatorId = creatorId;
        this.depositEntity = depositEntity;
    }

    public DepositCommentEntity update(String contents){
        this.contents = contents;
        return this;
    }

    public DepositCommentEntity delete(){
        this.deletedYn = "Y";
        return this;
    }

}

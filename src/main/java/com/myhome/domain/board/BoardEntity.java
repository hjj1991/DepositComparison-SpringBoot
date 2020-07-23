package com.myhome.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;



@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Getter
@Table(name="tbl_board")
@Entity
public class BoardEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "board_idx")
    private Long boardIdx;

    @Column(nullable = false, unique = true, length = 100)
    private String boardName;

    @Column(nullable=false)
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column(nullable=false, columnDefinition = "char(1) default 'N'")
    private String deletedYn;

    public BoardEntity update(String boardName, String deletedYn){
        this.boardName = boardName;
        this.deletedYn = deletedYn;
        return this;
    }

    @Builder
    public BoardEntity(String userId, String refreshToken, LocalDateTime createdDatetime, LocalDateTime expiredDatetime) {
        this.createdDatetime = createdDatetime;
    }
}




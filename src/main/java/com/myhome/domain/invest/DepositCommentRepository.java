package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepositCommentRepository extends JpaRepository<DepositCommentEntity, Long> {

    @Query("SELECT c FROM DepositCommentEntity c WHERE c.depositEntity.id=:depositEntityId AND c.deletedYn='N' order by c.commentIdx ASC")
    List<DepositCommentEntity> findCommentsOfDeposit(@Param("depositEntityId") Long depositEntityId);

    
}

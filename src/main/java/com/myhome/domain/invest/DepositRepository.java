package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DepositRepository extends JpaRepository<DepositEntity, Long> {
    @Query("select DISTINCT a from DepositEntity as a join fetch a.options join fetch a.bankInfo")
    List<DepositEntity> findAllJoinFetch();

    @Query("SELECT a FROM DepositEntity as a join fetch a.options b join fetch a.bankInfo GROUP BY a.finCoNo, a.finPrdtCd ORDER BY b.intrRate DESC")
    List<DepositEntity> findOrderByDeposit();

    DepositEntity findTopByFinPrdtCdAndFinCoNo(String finPrdtCd, String finCoNo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE DepositEntity as a SET a.deleteFlag = :flag")
    void updateTableDepositFlag(@Param("flag") int flag);

}

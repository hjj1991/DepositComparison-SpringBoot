package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface BankRepository extends JpaRepository<BankEntity, Long> {
    @Transactional
    BankEntity findFirstByFinCoNo(String finCoNo);


    @Transactional
    @Modifying
    @Query(value = "UPDATE BankEntity as a SET a.deleteFlag = :flag")
    void updateTableBankFlag(@Param("flag") int flag);

}

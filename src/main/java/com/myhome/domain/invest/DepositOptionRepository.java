package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface DepositOptionRepository extends JpaRepository<DepositOptionEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE Table tbl_deposit_option", nativeQuery = true)
    void truncateTableDepositOption();

}

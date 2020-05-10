package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface BankBranchRepository  extends JpaRepository<BankBranchEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE Table tbl_bank_branch", nativeQuery = true)
    void truncateTableBankBranch();
}

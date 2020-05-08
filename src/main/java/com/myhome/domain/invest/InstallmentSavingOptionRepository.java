package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface InstallmentSavingOptionRepository extends JpaRepository<InstallmentSavingOptionEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE Table tbl_installment_saving_option", nativeQuery = true)
    void truncateTableInstallmentSavingOption();
}

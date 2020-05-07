package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentSavingRepository extends JpaRepository<InstallmentSavingEntity, Long> {
    InstallmentSavingEntity findTopByFinPrdtCd(String finPrdtCd);
}

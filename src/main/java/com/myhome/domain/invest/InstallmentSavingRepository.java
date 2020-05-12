package com.myhome.domain.invest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface InstallmentSavingRepository extends JpaRepository<InstallmentSavingEntity, Long> {
    @Query("select DISTINCT a from InstallmentSavingEntity as a join fetch a.options join fetch a.bankInfo")
    List<InstallmentSavingEntity> findAllJoinFetch();

    InstallmentSavingEntity findTopByFinPrdtCd(String finPrdtCd);

}

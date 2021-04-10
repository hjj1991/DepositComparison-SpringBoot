package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstallmentSavingCommentRepository extends JpaRepository<InstallmentSavingCommentEntity, Long> {

    @Query("SELECT c FROM InstallmentSavingCommentEntity c WHERE c.installmentSavingEntity.id=:installmentSavingEntityId AND c.deletedYn='N' order by c.commentIdx ASC")
    List<InstallmentSavingCommentEntity> findCommentsOfInstallmentSaving(@Param("installmentSavingEntityId") Long installmentSavingEntityId);


}

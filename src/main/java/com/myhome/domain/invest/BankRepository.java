package com.myhome.domain.invest;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface BankRepository extends JpaRepository<BankEntity, Long> {
    @Transactional
    BankEntity findFirstByFinCoNo(String finCoNo);

}

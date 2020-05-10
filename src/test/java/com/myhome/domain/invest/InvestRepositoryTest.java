package com.myhome.domain.invest;

import com.myhome.domain.invest.service.BankService;
import com.myhome.domain.invest.service.InstallmentSavingService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.SysexMessage;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class InvestRepositoryTest {
    @Autowired
    InstallmentSavingOptionRepository installmentSavingOptionRepository;
    @Autowired
    BankBranchRepository bankBranchRepository;
    @Autowired
    InstallmentSavingService installmentSavingService;
    @Autowired
    InstallmentSavingRepository installmentSavingRepository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    BankService bankService;

//    @After
//    public void cleanup() {
//        installmentSavingRepository.deleteAll();
//    }

    @Test
    public void 은행목록넣기() throws Exception {
        bankRepository.save(
                BankEntity.builder()
                .calTel("칼전화")
                .dclsChrgMan("차지맨")
                .dclsMonth("몬뜨")
                .finCoNo("핀코넘")
                .hompUrl("홈피유알엘")
                .korCoNm("콜코넘")
                .build()
        );

        List<BankEntity> bankEntityList = bankRepository.findAll();

        Assertions.assertThat(bankEntityList.get(0).getCalTel()).isEqualTo("칼전화");
    }

    @Test
    public void 은행인서트() throws Exception{
        bankService.getBankList("020000");

        List<BankEntity> bankEntityList = bankRepository.findAll();

        System.out.println(bankEntityList.get(0).getFinCoNo());
    }

    @Test
    public void 은행검색() throws Exception{
        log.info("*****은행 정보 동기화 시작 ******");
        installmentSavingOptionRepository.truncateTableInstallmentSavingOption();
        installmentSavingRepository.deleteAllInBatch();
        bankBranchRepository.truncateTableBankBranch();
        bankRepository.deleteAllInBatch();
        bankService.getBankList("020000"); //은행
        bankService.getBankList("030300"); //저축은행
        log.info("*****은행 정보 동기화 종료 ******");
        installmentSavingService.getInstallmentSavingList("020000"); //은행
        installmentSavingService.getInstallmentSavingList("030300"); //저축은행
        log.info("*****적금 정보 동기화 종료 ******");
//        InstallmentSavingOptionEntity installmentSavingOptionEntity = new InstallmentSavingOptionEntity();
//        installmentSavingOptionEntity.setDclsMonth("1111");
//        installmentSavingOptionEntity.setFinCoNo("1111");
//        installmentSavingOptionEntity.setFinPrdtCd("2222");
//        installmentSavingOptionEntity.setIntrRate(Double.valueOf("22"));
//        installmentSavingOptionEntity.setIntrRate2(4.0);
//        installmentSavingOptionEntity.setIntrRateType("2222");
//        installmentSavingOptionEntity.setIntrRateTypeNm("5555");
//        installmentSavingOptionEntity.setRsrvType("555");
//        installmentSavingOptionEntity.setRsrvTypeNm("222");
//        installmentSavingOptionEntity.setSaveTrm("5555");
//
//        List<InstallmentSavingOptionEntity> installmentSavingOptionEntityList = new ArrayList<>();
//
//        InstallmentSavingEntity installmentSavingEntity = InstallmentSavingEntity.builder()
//                .dclsMonth("2222")
//                .etcNote("3333")
//                .finCoNo("1111")
//                .finCoSubmDay("2222")
//                .finPrdtCd("3333")
//                .finPrdtNm("4444")
//                .joinDeny("5555")
//                .joinMember("4444")
//                .joinWay("555")
//                .korCoNm("222")
//                .maxLimit(555)
//                .mtrtInt("333333")
//                .options(installmentSavingOptionEntityList)
//                .spclCnd("5555")
//                .build();
//        List<InstallmentSavingEntity> installmentSavingEntityList = new ArrayList<>();
//
//        installmentSavingRepository.save(installmentSavingEntity);
//
//        BankBranchEntity bankBranchEntity = BankBranchEntity.builder()
//                .areaCd("1234")
//                .areaNm("2222")
//                .dclsMonth("3333")
//                .exisYn("4444")
//                .finCoNo("2222")
//                .build();
////        bankBranchRepository.save(bankBranchEntity);
//        List<BankBranchEntity> bankBranchEntityList = new ArrayList<>();
////        bankBranchEntityList.add(bankBranchEntity);
////        List<BankBranchEntity> bankBranchEntityList = new ArrayList<>();
////        bankBranchEntityList.add(bankBranchRepository.findById(Long.valueOf("1")).orElseThrow());
//        BankEntity bankEntity = BankEntity.builder()
//                .bankRole("은행")
//                .korCoNm("1111")
//                .hompUrl("1234")
//                .finCoNo("2222")
//                .dclsMonth("2222")
//                .dclsChrgMan("3333")
//                .calTel("4444")
////                .bankBranchEntityList(bankBranchEntityList)
////                .installmentSavingEntityList(installmentSavingEntityList)
//                .build();
//        bankEntity.getBankBranchList().add(bankBranchEntity);
////        System.out.println("하이아힝히");
////        System.out.println(bankBranchRepository.findById(Long.valueOf("1")).orElseThrow().toString());
////        log.info(bankBranchRepository.findById(Long.valueOf("1")).orElseThrow().toString());
//        bankRepository.save(bankEntity);
//        System.out.println(bankRepository.findAll().get(0).getBankBranchList().get(0).getFinCoNo());
//        System.out.println("ㅎㅇㅎㅇ");
//        bankService.getBankList("020000"); //은행
//        bankService.getBankList("030300"); //저축은행
//        log.info("*****은행 정보 동기화 종료 ******");
//        installmentSavingService.getInstallmentSavingList("020000"); //은행
//        installmentSavingService.getInstallmentSavingList("030300"); //저축은행
//        log.info("*****적금 정보 동기화 종료 ******");
    }
}

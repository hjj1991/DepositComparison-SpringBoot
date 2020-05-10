package com.myhome.config;

import com.myhome.domain.invest.*;
import com.myhome.domain.invest.service.BankService;
import com.myhome.domain.invest.service.InstallmentSavingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j  // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor    //생성자 DI를 위한 lombok 어노테이션
@Configuration
public class InstallmentSavingSyncConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    @Autowired
    private StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음
    @Autowired
    private InstallmentSavingRepository installmentSavingRepository;
    @Autowired
    private InstallmentSavingOptionRepository installmentSavingOptionRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BankBranchRepository bankBranchRepository;
    @Autowired
    private BankService bankService;
    @Autowired
    private InstallmentSavingService installmentSavingService;
    @Autowired
    private SimpleJobLauncher jobLauncher;

        @Scheduled(cron = "0 0 20 * * *" )
//    @Scheduled(fixedDelay=1000 * 100000)
    @Transactional
    public void perform() throws Exception {
        System.out.println("Job Started at : " + new Date());
        JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution execution = jobLauncher.run(installmentSavingSyncJob(), param);

        System.out.println("Job finished with status: " + execution.getStatus());
    }

    @Bean
    public Job installmentSavingSyncJob() {
        log.info("************** This installmentSavingSyncJob ");
        return jobBuilderFactory.get("installmentSavingSyncJob")
                .preventRestart()
                .start(bankListSyncJobStep())
//                .next(installmentSavingSyncJobStep2())
                .build();
    }

    @Bean
    @JobScope
    public Step bankListSyncJobStep() {
        return stepBuilderFactory.get("bankListSyncJobStep")
        .tasklet((contribution, chunkContext) -> {
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
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    @JobScope
    public Step installmentSavingSyncJobStep2() {
        return stepBuilderFactory.get("installmentSavingSyncJobStep2")
                .tasklet((contribution, chunkContext) -> {

                    return RepeatStatus.FINISHED;
                }).build();
    }

//    @Bean
//    @JobScope
//    public Step installmentSavingSyncJobStep() {
//        log.info("************* This is installmentSavingSyncJobStep");
//
//        return stepBuilderFactory.get("installmentSavingSyncJobStep")
//                .<InstallmentSavingEntity, InstallmentSavingEntity> chunk(10)
//                .reader(installmentSavingReader())
//                .processor(installmentSavingProcessor())
//                .build();
//    }

//    @Bean
//    @JobScope
//    public ListItemReader<InstallmentSavingEntity> installmentSavingReader(){
//        log.info("********* This is installmentSavingEntityReader");
//        List<InstallmentSavingEntity> installmentSavingEntityList = installmentSavingService.getInstallmentSavingList("02000000");
//        log.info("********* getInstallmentSavingList SIZE : " + installmentSavingEntityList.size());
//        return new ListItemReader<>(installmentSavingEntityList);
//    }

    public ItemProcessor<InstallmentSavingEntity, InstallmentSavingEntity> installmentSavingProcessor() {
        return new ItemProcessor<InstallmentSavingEntity, InstallmentSavingEntity>() {
            @Override
            public InstallmentSavingEntity process(InstallmentSavingEntity item) throws Exception {
                log.info("********** This is installmentSavingProcessor");
                return item;
            }
        };
    }

    public ItemWriter<InstallmentSavingEntity> installmentSavingWriter() {
        installmentSavingOptionRepository.truncateTableInstallmentSavingOption();
        installmentSavingRepository.deleteAllInBatch();
        log.info("******* This is installmentSavingWriter");
        return ((List<? extends InstallmentSavingEntity> installmentSavingList) ->
                installmentSavingRepository.saveAll(installmentSavingList));
    }

    @Bean(name = "launcher")
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }



}

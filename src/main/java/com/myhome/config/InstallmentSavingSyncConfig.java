package com.myhome.config;

import com.myhome.domain.invest.InstallmentSavingEntity;
import com.myhome.domain.invest.InstallmentSavingOptionRepository;
import com.myhome.domain.invest.InstallmentSavingRepository;
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
    private InstallmentSavingService installmentSavingService;
    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Scheduled(fixedDelay=1000 * 60)
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
                .start(installmentSavingSyncJobStep())
                .build();
    }

    @Bean
    @JobScope
    public Step installmentSavingSyncJobStep() {
        log.info("************* This is installmentSavingSyncJobStep");

        return stepBuilderFactory.get("installmentSavingSyncJobStep")
                .<InstallmentSavingEntity, InstallmentSavingEntity> chunk(10)
                .reader(installmentSavingReader())
                .processor(installmentSavingProcessor())
                .writer(installmentSavingWriter())
                .build();
    }

    @Bean
    @JobScope
    public ListItemReader<InstallmentSavingEntity> installmentSavingReader(){
        log.info("********* This is installmentSavingEntityReader");
        List<InstallmentSavingEntity> installmentSavingEntityList = installmentSavingService.getInstallmentSavingList();
        log.info("********* getInstallmentSavingList SIZE : " + installmentSavingEntityList.size());
        return new ListItemReader<>(installmentSavingEntityList);
    }

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

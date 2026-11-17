
package com.banking.payment.orchestration.batch.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.banking.payment.orchestration.batch.exception.InvalidScheduledPaymentException;
import com.banking.payment.orchestration.batch.processor.ScheduledPaymentProcessor;
import com.banking.payment.orchestration.batch.writer.ScheduledPaymentWriter;
import com.banking.payment.orchestration.entity.ScheduledTransaction;

@Configuration
public class ScheduledPaymentBatchConfig {


@Bean
public Step scheduledPaymentStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        ItemReader<ScheduledTransaction> scheduledPaymentItemReader,
        ScheduledPaymentProcessor processor,
        ScheduledPaymentWriter writer,
        ScheduledPaymentSkipListener skipListener) {

    return new StepBuilder("scheduledPaymentStep", jobRepository)
            .<ScheduledTransaction, ScheduledTransaction>chunk(
                2, transactionManager
            )
            .reader(scheduledPaymentItemReader)
            .processor(processor)
            .writer(writer)
            .faultTolerant()
            .skip(InvalidScheduledPaymentException.class)
            .skipLimit(2)
            .listener(skipListener)
            .build();
}


    @Bean
    public Job scheduledPaymentJob(
            JobRepository jobRepository,
            Step scheduledPaymentStep) {

        return new JobBuilder(
                "scheduledPaymentJob", jobRepository)
                .start(scheduledPaymentStep)
                .build();
    }
}


package com.banking.payment.orchestration.batch.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.banking.payment.orchestration.batch.processor.ExcelPaymentProcessor;
import com.banking.payment.orchestration.batch.reader.ExcelPaymentBatchReader;
import com.banking.payment.orchestration.batch.writer.ExcelPaymentWriter;
import com.banking.payment.orchestration.entity.PaymentBatchItem;
import com.banking.payment.orchestration.repository.PaymentBatchItemRepository;

@Configuration
public class ExcelPaymentBatchConfig {

	@Bean
	public Step excelPaymentStep(
	        JobRepository jobRepository,
	        PlatformTransactionManager transactionManager,

	        @Qualifier("excelPaymentBatchReader")
	        ItemReader<PaymentBatchItem> reader,

	        ExcelPaymentProcessor processor,
	        ExcelPaymentWriter writer) {

	    return new StepBuilder("excelPaymentStep", jobRepository)
	            .<PaymentBatchItem, PaymentBatchItem>chunk(
	                    2, transactionManager)
	            .reader(reader)
	            .processor(processor)
	            .writer(writer)
	            .build();
	}

    @Bean
    public Job excelPaymentJob(
            JobRepository jobRepository,
            @Qualifier("excelPaymentStep") Step step) {

        return new JobBuilder("excelPaymentJob", jobRepository)
                .start(step)
                .build();
    }
    
    @Bean
    @StepScope
    public ItemReader<PaymentBatchItem> excelPaymentBatchReader(
            PaymentBatchItemRepository repository,
            @Value("#{jobParameters['batchId']}") Long batchId) {

        return new ExcelPaymentBatchReader(
                repository,
                batchId,
                3
        );
    }
}

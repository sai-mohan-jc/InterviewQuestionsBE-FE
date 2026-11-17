
package com.banking.payment.orchestration.batch.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.banking.payment.orchestration.entity.ScheduledTransaction;
import com.banking.payment.orchestration.repository.ScheduledTransactionRepository;

@Configuration
public class ScheduledPaymentReader {

    @Bean
    @StepScope
    public ItemReader<ScheduledTransaction> scheduledPaymentItemReader(
            ScheduledTransactionRepository repository) {

        return new KeysetScheduledPaymentReader(
                repository, 3);
    }
}

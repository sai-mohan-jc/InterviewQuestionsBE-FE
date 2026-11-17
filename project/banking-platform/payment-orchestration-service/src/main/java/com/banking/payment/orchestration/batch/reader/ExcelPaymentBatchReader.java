
package com.banking.payment.orchestration.batch.reader;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import com.banking.payment.orchestration.entity.PaymentBatchItem;
import com.banking.payment.orchestration.repository.PaymentBatchItemRepository;


public class ExcelPaymentBatchReader implements ItemReader<PaymentBatchItem> {

    private final PaymentBatchItemRepository repository;
    private final Long batchId;
    private final int pageSize;

    private final Queue<PaymentBatchItem> buffer = new ArrayDeque<>();

    private Long lastItemId = 0L;
    private boolean exhausted = false;

    public ExcelPaymentBatchReader(
            PaymentBatchItemRepository repository,
            Long batchId,
            int pageSize) {
        this.repository = repository;
        this.batchId = batchId;
        this.pageSize = pageSize;
    }

    @Override
    public PaymentBatchItem read() {

        if (buffer.isEmpty()) {

            if (exhausted) {
                return null;
            }

            List<PaymentBatchItem> records =
                    repository
                    .findByBatch_BatchIdAndItemStatusAndItemIdGreaterThanOrderByItemIdAsc(
                            batchId,
                            "PENDING",
                            lastItemId,
                            PageRequest.of(0, pageSize));

            if (records.isEmpty()) {
                exhausted = true;
                return null;
            }

            buffer.addAll(records);
        }

        PaymentBatchItem item = buffer.poll();

        lastItemId = item.getItemId();

        return item;
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

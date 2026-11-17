
package com.banking.payment.orchestration.batch.writer;

import java.util.List;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.banking.payment.orchestration.entity.PaymentBatchItem;
import com.banking.payment.orchestration.repository.PaymentBatchItemRepository;

@Component
public class ExcelPaymentWriter implements ItemWriter<PaymentBatchItem> {

    private final PaymentBatchItemRepository repository;

    public ExcelPaymentWriter(PaymentBatchItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends PaymentBatchItem> chunk) {

        List<? extends PaymentBatchItem> items = chunk.getItems();

        repository.saveAll(items);

        System.out.println("Excel batch chunk saved: " + items.size());
    }
}


package com.banking.payment.orchestration.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.payment.orchestration.entity.PaymentBatch;
import com.banking.payment.orchestration.entity.PaymentBatchItem;
import com.banking.payment.orchestration.repository.PaymentBatchItemRepository;
import com.banking.payment.orchestration.repository.PaymentBatchRepository;

@Service
public class PaymentBatchService {

    private final PaymentBatchRepository batchRepository;
    private final PaymentBatchItemRepository itemRepository;

    public PaymentBatchService(
            PaymentBatchRepository batchRepository,
            PaymentBatchItemRepository itemRepository) {
        this.batchRepository = batchRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public PaymentBatch saveBatch(List<PaymentBatchItem> items) {

        // Create batch header
        PaymentBatch batch = new PaymentBatch();

        batch.setBatchReference("BATCH-" + UUID.randomUUID());
        batch.setSourceType("EXCEL");
        batch.setTotalCount(items.size());
        batch.setSuccessCount(0);
        batch.setFailedCount(0);
        batch.setPendingCount(items.size());
        batch.setBatchStatus("UPLOADED");

        // Save parent record first
        PaymentBatch savedBatch = batchRepository.save(batch);

        // Associate each payment with the batch
        for (PaymentBatchItem item : items) {
            item.setBatch(savedBatch);
            item.setItemStatus("PENDING");
        }

        // Save all child records
        itemRepository.saveAll(items);

        return savedBatch;
    }
}

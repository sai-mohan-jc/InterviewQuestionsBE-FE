
package com.banking.payment.orchestration.batch.processor;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.banking.payment.orchestration.entity.PaymentBatchItem;

@Component
public class ExcelPaymentProcessor implements ItemProcessor<PaymentBatchItem, PaymentBatchItem> {

	@Override
	public PaymentBatchItem process(PaymentBatchItem item) {

		if (item.getAmount() == null || item.getAmount().signum() <= 0) {

			item.setItemStatus("FAILED");
			item.setFailureReason("Payment amount must be greater than zero");

		} else if (item.getFromAccount() == null || item.getToAccount() == null
				|| item.getFromAccount().equals(item.getToAccount())) {

			item.setItemStatus("FAILED");
			item.setFailureReason("Invalid source or destination account");

		} else {

			item.setItemStatus("PROCESSED");
			item.setFailureReason(null);
		}

		return item;
	}
}

package com.banking.payment.orchestration.batch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.banking.payment.orchestration.batch.reader.ExcelPaymentReader;
import com.banking.payment.orchestration.entity.PaymentBatch;
import com.banking.payment.orchestration.entity.PaymentBatchItem;
import com.banking.payment.orchestration.service.PaymentBatchService;

@RestController
@RequestMapping("/api/batches")
public class PaymentBatchController {
	private final PaymentBatchService paymentBatchService;
	private final ExcelPaymentReader excelPaymentReader;
	
	public PaymentBatchController(PaymentBatchService paymentBatchService,ExcelPaymentReader excelPaymentReader) {
		this.excelPaymentReader=excelPaymentReader;
		this.paymentBatchService=paymentBatchService;
	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
		
		List<PaymentBatchItem> items = excelPaymentReader.readExcel(file);

		PaymentBatch batch = paymentBatchService.saveBatch(items);

		return ResponseEntity.ok(
		        "Batch uploaded successfully. Batch Reference: "
		                + batch.getBatchReference()
		                + ", Total Payments: " + batch.getTotalCount()
		);
	}
}

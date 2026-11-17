package com.banking.payment.orchestration.batch.reader;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.banking.payment.orchestration.entity.PaymentBatchItem;

@Component
public class ExcelPaymentReader {

	public List<PaymentBatchItem> readExcel(MultipartFile file) throws Exception {

		List<PaymentBatchItem> items = new ArrayList<>();
		
		try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);

			// Skip header row (row 0)
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);

				if (row == null) {
					continue;
				}

				String paymentReference = getCellValue(row.getCell(0));

				String fromAccount = getCellValue(row.getCell(1));

				String toAccount = getCellValue(row.getCell(2));

				BigDecimal amount = new BigDecimal(getCellValue(row.getCell(3)));

				String beneficiaryName = getCellValue(row.getCell(4));

				PaymentBatchItem item = new PaymentBatchItem();

				item.setPaymentReference(paymentReference);
				item.setFromAccount(fromAccount);
				item.setToAccount(toAccount);
				item.setAmount(amount);
				item.setBeneficiaryName(beneficiaryName);

				items.add(item);
				
				System.out.println(item);

			}
		}
		
		return items;
	}

	private String getCellValue(Cell cell) {

		if (cell == null) {
			return "";
		}

		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell).trim();
	}
}

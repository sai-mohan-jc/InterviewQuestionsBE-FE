
package com.banking.payment.orchestration.batch.scheduler;

import java.util.List;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.banking.payment.orchestration.entity.PaymentBatch;
import com.banking.payment.orchestration.repository.PaymentBatchRepository;

@Component
public class ExcelPaymentScheduler {

    private final PaymentBatchRepository batchRepository;
    private final JobOperator jobOperator;
    private final Job excelPaymentJob;

    public ExcelPaymentScheduler(
            PaymentBatchRepository batchRepository,
            JobOperator jobOperator,
            @Qualifier("excelPaymentJob") Job excelPaymentJob) {

        this.batchRepository = batchRepository;
        this.jobOperator = jobOperator;
        this.excelPaymentJob = excelPaymentJob;
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 10000)
    public void processUploadedBatches() {

        List<PaymentBatch> batches =
                batchRepository.findByBatchStatus("UPLOADED");

        for (PaymentBatch batch : batches) {

            try {
                batch.setBatchStatus("PROCESSING");
                batchRepository.save(batch);

                JobParameters parameters =
                        new JobParametersBuilder()
                        .addLong("batchId", batch.getBatchId())
                        .toJobParameters();

                JobExecution execution =
                        jobOperator.start(excelPaymentJob, parameters);

                System.out.println(
                        "Excel Batch Started: "
                        + batch.getBatchReference()
                        + ", Execution ID: "
                        + execution.getId());

            } catch (Exception exception) {

                batch.setBatchStatus("UPLOADED");
                batchRepository.save(batch);

                exception.printStackTrace();
            }
        }
    }
}

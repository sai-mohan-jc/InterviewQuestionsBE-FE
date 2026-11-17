
package com.banking.payment.orchestration.batch.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class ScheduledPaymentBatchController {

	private final JobOperator jobOperator;
	private final Job scheduledPaymentJob;

	public ScheduledPaymentBatchController(JobOperator jobOperator, Job scheduledPaymentJob) {
		this.jobOperator = jobOperator;
		this.scheduledPaymentJob = scheduledPaymentJob;
	}

	@PostMapping("/scheduled-payments/run")
	public Map<String, Object> runScheduledPaymentJob() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder().addLocalDateTime("run.timestamp", LocalDateTime.now())
				.toJobParameters();

		JobExecution execution = jobOperator.start(scheduledPaymentJob, jobParameters);

		return Map.of("jobName", scheduledPaymentJob.getName(), "executionId", execution.getId(), "status",
				execution.getStatus().toString(), "exitStatus", execution.getExitStatus().getExitCode());
	}
}

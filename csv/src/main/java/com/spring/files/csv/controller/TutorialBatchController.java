package com.spring.files.csv.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TutorialBatchController {
	
	private final JobLauncher jobLauncher;

	private final Job job;

	public TutorialBatchController(JobLauncher jobLauncher, Job job) {
		super();
		this.jobLauncher = jobLauncher;
		this.job = job;
	}

	@GetMapping(value = "/startBatch")
	public void startBatch() throws JobInstanceAlreadyCompleteException, 
	                                JobExecutionAlreadyRunningException,
	                                JobParametersInvalidException,
	                                JobRestartException	{
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("startAt", System.currentTimeMillis()).toJobParameters();
		JobExecution run = jobLauncher.run(job, jobParameters);
		run.getStatus();
	}
}


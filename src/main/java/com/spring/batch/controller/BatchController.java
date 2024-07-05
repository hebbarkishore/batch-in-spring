package com.spring.batch.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("codingWorld")
public class BatchController {

	@Autowired
	Job job;
	
	@Autowired
	JobLauncher jobLauncher;
	
	
	@GetMapping("/runPersistJob")
	public BatchStatus readPersistFile() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("time", new JobParameter(new Date().getTime()));
		JobParameters jobParameters = new JobParameters(params);
		
		
//		JobParameters jobParameters = new JobParametersBuilder()
//                .addString("filePath", filePath)
//                .addLong("time", System.currentTimeMillis())
//                .toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		System.out.println(jobExecution.getStatus());
		while(jobExecution.isRunning()) {
			System.out.println("running...");
		}
		
		return jobExecution.getStatus();
	}
}

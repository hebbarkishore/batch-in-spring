package com.spring.batch.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.spring.batch.model.CarDetails;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Bean
    TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
	
	
	@Bean
	Job getJob(JobBuilderFactory jobBuilderFactory, 
			   StepBuilderFactory stepBuilderFactory,
			ItemReader<CarDetails> itemReader,
			ItemProcessor<CarDetails, CarDetails> itemProcessor,
			ItemWriter<CarDetails> itemWriter) {
		Step step = stepBuilderFactory.get("car-details-step")
				.<CarDetails, CarDetails>chunk(3) //this is because chunk requires input and output type of similar to processor
				.reader(itemReader)  //reads data from file and convert to object
				.processor(itemProcessor) //this takes the input as functional interface, so we have to create one and implement the process
				.writer(itemWriter) //this calls the write method 
				.taskExecutor(taskExecutor()) // optional to Set the task executor for multi-threaded chunk
	            .throttleLimit(4) //optional if you need to go with the multi-threaded chunk
				.build();
	
		return jobBuilderFactory
				.get("car-details-job") //job name
				.incrementer(new RunIdIncrementer()) //batch run ID
				.start(step) //for multiple steps use .flow(step).next(step)...so on
				.build();
	}	
	
	@Bean
	@StepScope
     FlatFileItemReader<CarDetails> itemReader() {
		System.out.println("entered to itemReader");
		FlatFileItemReader<CarDetails> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource("src/main/resources/largeFile.csv"));
		//flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("Flat file reader");	
		flatFileItemReader.setLineMapper(getLineMapper());
		flatFileItemReader.setLinesToSkip(1);
		return flatFileItemReader;
	}

	private LineMapper<CarDetails> getLineMapper() {
		System.out.println("entered to getLineMapper");
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("carId", "make", "model", "year");
		
		BeanWrapperFieldSetMapper<CarDetails> fieldSetMapper= new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(CarDetails.class);
		
		DefaultLineMapper<CarDetails> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		return defaultLineMapper;
	}
}

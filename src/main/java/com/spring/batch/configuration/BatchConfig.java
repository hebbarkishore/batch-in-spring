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

import com.spring.batch.model.CarDetails;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Bean
	Job getJob(JobBuilderFactory jobBuilderFactory, 
			   StepBuilderFactory stepBuilderFactory,
			ItemReader<CarDetails> itemReader,
			ItemProcessor<CarDetails, CarDetails> itemProcessor,
			ItemWriter<CarDetails> itemWriter) {
		Step step = stepBuilderFactory.get("car-details-step")
				.<CarDetails, CarDetails>chunk(100) //this is because chunk requires input and output type of similar to processor
				.reader(itemReader) 
				.processor(itemProcessor) //this takes the input as functional interface, so we have to create one and implement the process
				.writer(itemWriter)
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

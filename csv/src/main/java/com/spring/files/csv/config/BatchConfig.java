package com.spring.files.csv.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.files.csv.model.Tutorial;
import com.spring.files.csv.repository.TutorialRepository;

@Configuration
public class BatchConfig {
	
	@Autowired
	private final TutorialRepository repository;

	public BatchConfig(TutorialRepository repository) {
		super();
		this.repository = repository;
	}

	public FlatFileItemReader<Tutorial> itemReader() {
		FlatFileItemReader<Tutorial> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/Subjects.csv"));
		itemReader.setName("csv-reader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<Tutorial> lineMapper() {
		DefaultLineMapper<Tutorial> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames("id", "title", "description", "published");
		tokenizer.setStrict(false);
		
		BeanWrapperFieldSetMapper<Tutorial> mapper = new BeanWrapperFieldSetMapper<>();
		mapper.setTargetType(Tutorial.class);
		lineMapper.setFieldSetMapper(mapper);
		lineMapper.setLineTokenizer(tokenizer);
		return lineMapper;
	}
	
	@Bean
	public TutorialProcessor processor() {
		return new TutorialProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Tutorial> itemWriter() {
		RepositoryItemWriter<Tutorial> writer = new RepositoryItemWriter<>();
		writer.setRepository(repository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("csv_step",jobRepository)
				.<Tutorial, Tutorial>chunk(2, transactionManager)
				.reader(itemReader())
				.processor(processor())
				.writer(itemWriter())
				.taskExecutor(taskExecutor())
				.build();
	}
	
	private TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(2);
		return asyncTaskExecutor;
	}
	
	@Bean
	public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("csv-job", jobRepository)
				.flow(step(jobRepository, transactionManager)).end().build();
	}
}

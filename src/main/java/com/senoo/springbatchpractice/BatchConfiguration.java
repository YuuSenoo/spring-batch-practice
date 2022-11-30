package com.senoo.springbatchpractice;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .start(sampleStep)
                .build();
    }

    @Bean
    public Step sampleStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            TestSummaryProcessor testSummaryProcessor) {
        return new StepBuilder("sampleStep", jobRepository)
                .<TestScores, TestSummary>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(testSummaryProcessor)
                .writer(itemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<TestScores> itemReader() {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<TestScores>();
        fieldSetMapper.setTargetType(TestScores.class);

        var lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setQuoteCharacter('"');
        lineTokenizer.setNames(new String[] {
                "studentId",
                "japaneseScore",
                "mathScore",
                "scienceScore",
                "societyScore",
                "englishScore"
        });

        var lineMapper = new DefaultLineMapper<TestScores>();
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(lineTokenizer);

        return new FlatFileItemReaderBuilder<TestScores>()
                .name("testScoresReader")
                .resource(new FileSystemResource("src/main/resources/TestScores.csv"))
                .encoding("UTF-8")
                .lineMapper(lineMapper)
                .build();
    }

    @Bean
    public FlatFileItemWriter<TestSummary> itemWriter() {
        var fieldExtractor = new BeanWrapperFieldExtractor<TestSummary>();
        fieldExtractor.setNames(new String[] {
                "studentId",
                "totalScore"
        });
        fieldExtractor.afterPropertiesSet();

        var lineAggregator = new CustomDelimitedLineAggregator<TestSummary>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<TestSummary>()
                .name("testSummaryWriter")
                .resource(new FileSystemResource("target/test-outputs/output.csv"))
                .lineAggregator(lineAggregator)
                .encoding("UTF-8")
                .build();
    }

}

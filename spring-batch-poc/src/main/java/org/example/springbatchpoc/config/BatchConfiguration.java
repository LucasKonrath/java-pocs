package org.example.springbatchpoc.config;

import org.example.springbatchpoc.listener.JobCompletionNotificationListener;
import org.example.springbatchpoc.model.Employee;
import org.example.springbatchpoc.processor.EmployeeItemProcessor;
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
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    @Bean
    public FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .resource(new ClassPathResource("input-employees.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName", "email", "department", "salary"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                    setTargetType(Employee.class);
                }})
                .linesToSkip(1) // Skip header line
                .build();
    }

    @Bean
    public FlatFileItemWriter<Employee> writer() {
        return new FlatFileItemWriterBuilder<Employee>()
                .name("employeeItemWriter")
                .resource(new FileSystemResource("output/processed-employees.csv"))
                .lineAggregator(new DelimitedLineAggregator<Employee>() {{
                    setDelimiter(",");
                    setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {{
                        setNames(new String[]{"firstName", "lastName", "email", "department", "salary"});
                    }});
                }})
                .headerCallback(writer -> writer.write("firstName,lastName,email,department,salary"))
                .build();
    }

    @Bean
    public Job processEmployeeJob(JobRepository jobRepository,
                                  Step step1,
                                  JobCompletionNotificationListener listener) {
        return new JobBuilder("processEmployeeJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      FlatFileItemReader<Employee> reader,
                      EmployeeItemProcessor processor,
                      FlatFileItemWriter<Employee> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Employee, Employee>chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}

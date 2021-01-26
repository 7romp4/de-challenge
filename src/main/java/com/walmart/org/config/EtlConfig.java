package com.walmart.org.config;

import com.walmart.org.modelo.Company;
import com.walmart.org.modelo.Score;
import com.walmart.org.modelo.dto.CompanyItemProcessor;
import com.walmart.org.modelo.dto.ConsoleItemProcessor;
import com.walmart.org.modelo.dto.ConsoleReader;
import com.walmart.org.modelo.dto.ReportTopTenAll;
import com.walmart.org.modelo.dto.ReportTopTenConsole;
import com.walmart.org.modelo.dto.ReportWorstTenAll;
import com.walmart.org.modelo.dto.ReportWorstTenConsole;
import com.walmart.org.modelo.dto.ScoreItemProcessor;
import com.walmart.org.modelo.dto.ScoreReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class EtlConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader consoleReader() {
        FlatFileItemReader reader = new FlatFileItemReaderBuilder().name("consolesItemReader")
                .resource(new ClassPathResource("consoles.csv"))
                .delimited()
                .names(new String[] { "console", "company"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
                    setTargetType(ConsoleReader.class);
                }})
                .build();
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public FlatFileItemReader scoreReader(){
        FlatFileItemReader reader = new FlatFileItemReaderBuilder().name("scoreItemReader")
                .resource(new ClassPathResource("result.csv"))
                .delimited()
                .names(new String[] { "metaScore", "name","console","userScore","date"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
                    setTargetType(ScoreReader.class);
                }})
                .build();
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Company> writerCompany(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO company (name) VALUES (:name)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Company> writerConsole(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO console (name,company_id) VALUES (:name,:company.id)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Score> writerScore(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO score (name,metascore,console_id,userscore,date) VALUES (:name,:metascore,:console.id,:userscore,:date)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ConsoleItemProcessor consoleProcessor() {
        return new ConsoleItemProcessor();
    }

    @Bean
    public CompanyItemProcessor companyProcessor(){
        return new CompanyItemProcessor();
    }

    @Bean
    public ScoreItemProcessor scoreProcessor(){
        return new ScoreItemProcessor();
    }
    
    @Bean
    public ReportTopTenConsole reportTopTenConsole() {
    	return new ReportTopTenConsole();
    }
    
    @Bean
    public ReportWorstTenConsole reportWorstTenConsole() {
    	return new ReportWorstTenConsole();
    }
    
    @Bean
    public ReportTopTenAll reportTopTenAll() {
    	return new ReportTopTenAll();
    }
    
    @Bean
    public ReportWorstTenAll reportWorstTenAll() {
    	return new ReportWorstTenAll();
    }


    @Bean
    public Step step1(JdbcBatchItemWriter writerCompany) {
        return stepBuilderFactory.get("step1")
                .<ConsoleReader,ConsoleReader> chunk(10)
                .reader(consoleReader())
                .processor(companyProcessor())
                .writer(writerCompany)
                .build();
    }

    @Bean
    public Step step2(JdbcBatchItemWriter writerConsole) {
        return stepBuilderFactory.get("step2")
                .<ConsoleReader,ConsoleReader> chunk(10)
                .reader(consoleReader())
                .processor(consoleProcessor())
                .writer(writerConsole)
                .build();
    }

    @Bean
    public Step step3(JdbcBatchItemWriter writerScore){
        return stepBuilderFactory.get("step3")
                .<ConsoleReader,ConsoleReader> chunk(10)
                .reader(scoreReader())
                .processor(scoreProcessor())
                .writer(writerScore)
                .build();
    }
    
    @Bean
    public Step step4(){
        return stepBuilderFactory.get("step4")
                .tasklet(reportTopTenConsole())
                .build();
    }
    
    @Bean
    public Step step5(){
        return stepBuilderFactory.get("step5")
                .tasklet(reportWorstTenConsole())
                .build();
    }
    
    @Bean
    public Step step6(){
        return stepBuilderFactory.get("step6")
                .tasklet(reportTopTenAll())
                .build();
    }
    
    @Bean
    public Step step7(){
        return stepBuilderFactory.get("step7")
                .tasklet(reportWorstTenAll())
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1,Step step2,Step step3,Step step4,Step step5,
    		Step step6,Step step7) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .next(step2)
                .next(step3)
                .next(step4)
                .next(step5)
                .next(step6)
                .next(step7)
                .build();
    }
}

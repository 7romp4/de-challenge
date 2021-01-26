package com.walmart.org.config;

import com.walmart.org.modelo.Company;
import com.walmart.org.modelo.Console;
import com.walmart.org.modelo.Score;
import com.walmart.org.repo.CompanyRepo;
import com.walmart.org.repo.ConsoleRepo;
import com.walmart.org.repo.ScoreRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private ConsoleRepo consoleRepo;
    
    @Autowired
    private ScoreRepo scoreRepo;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                       
            System.out.println("!!! JOB FINISHED! Time to verify the results");
        }
    }
}

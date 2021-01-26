package com.walmart.org.modelo.dto;

import com.walmart.org.modelo.Company;
import com.walmart.org.modelo.Console;
import com.walmart.org.repo.CompanyRepo;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ConsoleItemProcessor implements ItemProcessor<ConsoleReader, Console> {

    @Autowired
    private CompanyRepo companyRepo;

    private List<Company> companies;

    @BeforeStep
    public void before(StepExecution stepExecution) {
        companies = companyRepo.findAll();
    }

    @Override
    public Console process(ConsoleReader consoleReader) throws Exception {

        Console console = new Console();
        companies.forEach(company -> {
            if(company.getName().equals(consoleReader.getCompany())){
                console.setCompany(company);
            }
        });
        console.setName(consoleReader.getConsole());
        return console;
    }
}

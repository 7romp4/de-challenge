package com.walmart.org.modelo.dto;

import com.walmart.org.modelo.Company;
import com.walmart.org.modelo.Console;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class CompanyItemProcessor implements ItemProcessor<ConsoleReader, Company> {

    private Set<String> companies = new HashSet<>();

    @Override
    public Company process(ConsoleReader consoleReader) throws Exception {
        if(companies.contains(consoleReader.getCompany())){
            return null;
        }
        companies.add(consoleReader.getCompany());
        Company company = new Company();
        company.setName(consoleReader.getCompany());
        return company;
    }
}

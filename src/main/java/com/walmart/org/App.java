package com.walmart.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Create your own Microservice here, You are free to create all the folders you want
 * We give you the Maven Archetype with the Hello World :)
 *
 */
@SpringBootApplication
public class App{
    public static void main( String[] args ){
        SpringApplication.run(App.class, args);
    }
}

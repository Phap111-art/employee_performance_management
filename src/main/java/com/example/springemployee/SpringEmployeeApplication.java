package com.example.springemployee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.example.springemployee")
public class SpringEmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEmployeeApplication.class, args);
    }

}

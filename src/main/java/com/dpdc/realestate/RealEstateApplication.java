package com.dpdc.realestate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class RealEstateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstateApplication.class, args);
    }

}

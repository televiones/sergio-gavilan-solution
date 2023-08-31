package com.napptilustest.sergiogavilansolution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SergioGavilanSolutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SergioGavilanSolutionApplication.class, args);
    }

}

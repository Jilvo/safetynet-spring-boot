package com.safetynet.alerts;

import com.safetynet.alerts.services.JsonFileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.safetynet.alerts.controllers")
public class DemoApplication {


    @Bean
    public JsonFileService jsonFileService() {
        return new JsonFileService();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

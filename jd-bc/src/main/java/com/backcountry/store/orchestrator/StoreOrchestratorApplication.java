package com.backcountry.store.orchestrator;

import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:CamelConfig.xml")
@EnableAutoConfiguration(exclude = {CamelAutoConfiguration.class})
public class StoreOrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreOrchestratorApplication.class, args);
    }
}

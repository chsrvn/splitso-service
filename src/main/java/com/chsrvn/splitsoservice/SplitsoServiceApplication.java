package com.chsrvn.splitsoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class SplitsoServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(SplitsoServiceApplication.class);

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(SplitsoServiceApplication.class, args);
            logger.info("========================================");
            logger.info("Application started successfully!");
            logger.info("========================================");
        } catch (Exception e) {
            logger.error("Failed to start application", e);
            throw e;
        }
    }

}

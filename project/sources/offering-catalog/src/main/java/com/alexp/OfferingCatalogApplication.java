package com.alexp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;

@SpringBootApplication
public class OfferingCatalogApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferingCatalogApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OfferingCatalogApplication.class, args);
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long mb = 1024 * 1024;
        String mega = " MB";

        LOGGER.info("========================== Memory Info ==========================");
        LOGGER.info("Free memory: " + format.format(freeMemory / mb) + mega);
        LOGGER.info("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
        LOGGER.info("Max memory: " + format.format(maxMemory / mb) + mega);
        LOGGER.info("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        LOGGER.info("=================================================================\n");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

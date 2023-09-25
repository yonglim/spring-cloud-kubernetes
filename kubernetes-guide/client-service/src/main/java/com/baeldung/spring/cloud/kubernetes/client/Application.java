package com.baeldung.spring.cloud.kubernetes.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@RibbonClient(name = "travel-agency-service", configuration = RibbonConfiguration.class)
public class Application {

    private static final Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) {
        log.info("Client Service Started! *** 77 ");
        System.out.println( "Client Service Started! ## 77 ");
        SpringApplication.run(Application.class, args);
    }

}

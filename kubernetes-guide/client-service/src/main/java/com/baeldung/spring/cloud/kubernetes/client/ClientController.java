package com.baeldung.spring.cloud.kubernetes.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ClientConfig config;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private TravelAgencyService travelAgencyService;

    private static final Log log = LogFactory.getLog(ClientController.class);

    @RequestMapping("/deals")
    public String getDeals() {
        log.info( " getting deals 88 ");
        System.out.println( " getting deals ** 88 ");
        return "getting deals from service " + travelAgencyService.getDeals();
    }

    @RequestMapping("/circuit")
    public String getDelayWithCircuitBreaker(@RequestParam Integer delaySeconds) {
        log.info( " circuit breaker 88 .. delaySeconds : " + delaySeconds);
        System.out.println( " circuit breaker ** 88 .. delaySeconds : " + delaySeconds);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("client-service");
        return circuitBreaker.run(() -> "getting circuit from service " + travelAgencyService.getDelay(delaySeconds), throwable -> getFallbackMessage());
    }

    private String getFallbackMessage() {
        return "{ \"message\": \"fallback message\" }";
    }

    @GetMapping
    public String load() {
        log.info( " loading deals 77 ");
        System.out.println( " loading deals ** 77 ");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://travel-agency-service:8080";
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);

        String serviceList = "";
        if (discoveryClient != null) {
            List<String> services = this.discoveryClient.getServices();
            log.info( " services " + services);

            for (String service : services) {

                List<ServiceInstance> instances = this.discoveryClient.getInstances(service);

                serviceList += ("[" + service + " : " + ((!CollectionUtils.isEmpty(instances)) ? instances.size() : 0) + " instances ]");
            }
        }

        log.info( " serviceList " + serviceList);
        return String.format(config.getMessage(), response.getBody(), serviceList);
    }
}

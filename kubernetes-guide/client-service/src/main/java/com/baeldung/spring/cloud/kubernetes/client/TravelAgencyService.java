package com.baeldung.spring.cloud.kubernetes.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class TravelAgencyService {

    private final RestTemplate restTemplate;

    public TravelAgencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackName", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000") })
    public String getDeals() {
        return this.restTemplate.getForObject("http://travel-agency-service:8080/deals", String.class);
    }

    public String getDelay(Integer delayInSeconds) {
        System.out.println( " getDelay method ...  delayInSeconds : " +  delayInSeconds);

//        return this.restTemplate.getForObject("http://travel-agency-service:8080/delay", String.class);

        // https://stackoverflow.com/questions/8297215/spring-resttemplate-get-with-parameters
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://travel-agency-service:8080/delay")
                .queryParam("delaySeconds", "{delaySeconds}")
                .encode()
                .toUriString();

        Map<String, Integer> params = Map.of("delaySeconds", delayInSeconds);

        HttpEntity<String> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                String.class,
                params
        );

        System.out.println( " response  : " + response);

        return response.getBody();

    }

    private String getFallbackName() {
        return "Fallback";
    }
}

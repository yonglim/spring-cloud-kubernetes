package com.baeldung;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class TestClientEndpoint {

    @Test
    @Ignore
    public void testEndpoint() throws URISyntaxException, IOException, InterruptedException {
        // https://www.baeldung.com/java-9-http-client

        Random random = new Random();

        for (int i = 0; i < 20; i++) {

            Runnable testTask = () -> {
                HttpResponse<String> response = null;
                try {
                    Integer delaySeconds = random.nextInt(1, 15);
                    System.out.println( " delaySeconds : " + delaySeconds);
                    HttpRequest request = HttpRequest.newBuilder(new URI("http://127.0.0.1:56485/circuit?delaySeconds=" + delaySeconds.toString()))
                            .GET()
                            .build();
                    response = HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());
                } catch (Exception e) {
                    System.out.println(" exception : " + e);
                }

                System.out.println( " response : " + response);
                System.out.println( " response : " + response.body());
            };
            new Thread(testTask).start();

            Integer sleepSecond = random.nextInt(1, 3);
            System.out.println( " sleep seconds : " + sleepSecond);
            Thread.sleep(1000 * sleepSecond);
        }

    }
}

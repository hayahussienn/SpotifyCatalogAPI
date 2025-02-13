package com.example.catalog;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpLoadTestAsync {
    private static final String TARGET_URL = "http://localhost:8080";
    private static final int REQUEST_COUNT = 1000;

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        Instant testStart = Instant.now();

        for (int i = 1; i <= REQUEST_COUNT; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TARGET_URL))
                    .GET()
                    .build();

            int requestNumber = i;

            CompletableFuture<Void> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        long duration = Duration.between(testStart, Instant.now()).toMillis();
                        int statusCode = response.statusCode();

                        if (statusCode >= 200 && statusCode < 300) {
                            System.out.println("Request #" + requestNumber + " succeeded with status " + statusCode + " in " + duration + " ms");
                        } else {
                            System.err.println("Request #" + requestNumber + " failed with status " + statusCode + " in " + duration + " ms");
                        }
                    })
                    .exceptionally(e -> {
                        long duration = Duration.between(testStart, Instant.now()).toMillis();
                        System.err.println("Request #" + requestNumber + " failed due to exception: " + e.getMessage() + " in " + duration + " ms");
                        return null;
                    });

            futures.add(future);
            System.out.println("Request #" + requestNumber + " sent asynchronously");
        }


        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();

        Instant testEnd = Instant.now();
        System.out.println("All requests completed in " + Duration.between(testStart, testEnd).toMillis() + " ms");
    }
}

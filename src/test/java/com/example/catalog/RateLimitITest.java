package com.example.catalog;

import com.example.catalog.interceptors.RateLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitITest
{

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RateLimit rateLimit;  // Inject the RateLimit class to reset the state

    private static final String API_ENDPOINT = "/";
    private static final String INTERNAL_ENDPOINT = "/internal";
    private static final String XRateLimitRetryAfterSecondsHeader = "X-Rate-Limit-Retry-After-Seconds";
    private static final String XRateLimitRemaining = "X-Rate-Limit-Remaining";


    @BeforeEach
    public void resetRateLimiterState() {
        // Reset the state of the rate limiter for each test to ensure no interference
        rateLimit.resetRequestLogs();
    }

    @Test
    public void testRateLimiterEnforcesLimits() throws InterruptedException {
        int allowedRequests = 10; // Number of allowed requests
        int extraRequests = 5;    // Additional requests to test exceeding the limit
        // Send requests within the allowed limit
        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)), "Expected status code to be 200 for the first 10 requests");

            String remainingRequests = String.valueOf(allowedRequests - (i + 1));
            assertEquals(remainingRequests, response.getHeaders().get(XRateLimitRemaining).get(0), "Expected " + XRateLimitRemaining + " header to be " + remainingRequests + " after " + i + 1 + " requests");
        }
        // Send requests exceeding the allowed limit
        for (int i = 0; i < extraRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(429)));
            int retryAfter = Integer.parseInt(response.getHeaders().get(XRateLimitRetryAfterSecondsHeader).get(0));
            assertTrue(retryAfter > 0);
        }
    }

    @Test
    public void testRateLimiterBypassesInternalEndpoint() {
        int totalRequests = 15;
        // Send requests to the internal endpoint which should bypass rate limiting
        for (int i = 0; i < totalRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(INTERNAL_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)));
            assertFalse(response.getHeaders().containsKey(XRateLimitRemaining));
        }
    }

    @Test
    public void testRateLimiterAtThreshold() {
        int allowedRequests = 10; // Number of allowed requests

        // Send exactly 10 requests, which should hit the rate limit
        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)),
                    "Expected status code to be 200 for the first 10 requests");

            String remainingRequests = String.valueOf(allowedRequests - (i + 1));
            assertEquals(remainingRequests, response.getHeaders().get(XRateLimitRemaining).get(0),
                    "Expected " + XRateLimitRemaining + " header to be " + remainingRequests + " after " + i + 1 + " requests");
        }

        // Send one more request to exceed the rate limit
        ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
        assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(429)),
                "Expected status code 429 after hitting rate limit");

        int retryAfter = Integer.parseInt(response.getHeaders().get(XRateLimitRetryAfterSecondsHeader).get(0));
        assertTrue(retryAfter > 0, "Expected retry-after header to be set");
    }


    @Test
    public void testRateLimiterResetsAfterTimeWindow() throws InterruptedException {
        int allowedRequests = 10;

        // Send 10 requests
        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)),
                    "Expected status code 200 for request " + (i + 1));
        }
        // Wait for more than 1 minute to let the rate limit reset
        Thread.sleep(60000); // Wait for 60 seconds

        // Send another request after the time window has passed
        ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
        assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)),
                "Expected status code 200 after the time window resets");
    }

//    @Test
//    public void testRateLimiterDisabled() {
// this test will pass when we change rate-limit.enabled to false (in application.properties)
//        // Set rate limiting to be disabled (assuming there's a configuration to turn it off)
//        System.setProperty("rate-limit.enabled", "false");
//
//        int totalRequests = 20;
//
//        // Send 20 requests (shouldn't be rate limited because rate limit is disabled)
//        for (int i = 0; i < totalRequests; i++) {
//            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
//            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)),
//                    "Expected status code 200 for all requests when rate limit is disabled");
//        }
//    }
}
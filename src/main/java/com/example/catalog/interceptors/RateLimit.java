package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class RateLimit implements HandlerInterceptor
{

    @Value("${rate-limit.algo}")
    private String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    private int rateLimitRPM;  // Requests per minute (rate limit)

    @Value("${rate-limit.enabled}")
    private boolean isRateLimitEnabled; // Configurable flag to enable/disable rate limiting

    private final Map<String, Deque<Long>> requestLogs = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();  // To ensure thread safety

    // Time window duration in seconds
    private static final long TIME_WINDOW = 60;  // 1 minute for example

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();

        // Skip rate limiting for the /internal endpoint
        if (request.getRequestURI().startsWith("/internal")) {
            return true;
        }
        // Skip rate limiting if disabled
        if (!isRateLimitEnabled) {
            return true;
        }

        lock.lock();
        try {
            if (!isAllowed(clientIp))
            {
                // If rate limit is exceeded, inform the client with headers
                response.setHeader("X-Rate-Limit-Remaining", "0");
                response.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(TIME_WINDOW));
                response.setStatus(429); // Too many requests
                return false;
            }

            // Otherwise, allow the request and set the remaining requests
            long remainingRequests = rateLimitRPM - requestLogs.get(clientIp).size();
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(remainingRequests));
            response.setStatus(200);
            return true;

        } finally {
            lock.unlock();
        }
    }

    private boolean isAllowed(String clientIp)
    {

        if (rateLimitAlgo == "moving" +
                "") {
            long currentTime = System.currentTimeMillis() / 1000; // Current time in seconds
            Deque<Long> requests = requestLogs.getOrDefault(clientIp, new LinkedList<>());
            // Remove old requests outside the time window
            while (!requests.isEmpty() && requests.peekFirst() <= currentTime - TIME_WINDOW) {
                requests.pollFirst();  // Remove requests outside the time window
            }

            // If the number of requests exceeds the allowed limit block the request
            if (requests.size() >= rateLimitRPM) {
                return false;  // Block the request
            }

            // Add the current request timestamp to the deque
            requests.addLast(currentTime);
            requestLogs.put(clientIp, requests);  // Update the request log for the client
            return true;
        } else {
            // Case of fixed window size
            long currentTimeMillis = System.currentTimeMillis();
            long windowStartTime = (currentTimeMillis / (TIME_WINDOW * 1000)) * (TIME_WINDOW * 1000);

            // Initialize or retrieve the fixed window for the client
            Deque<Long> requests = requestLogs.getOrDefault(clientIp, new LinkedList<>());
            if (requests.isEmpty() || requests.peekFirst() < windowStartTime) {
                requests.clear(); // Clear requests from the previous window
            }

            // If the number of requests exceeds the allowed limit, block the request
            if (requests.size() >= rateLimitRPM)
            {
                return false;  // Block the request
            }

            // Add the current request timestamp to the deque
            requests.addLast(currentTimeMillis);
            requestLogs.put(clientIp, requests);  // Update the request log for the client
            return true;
        }
    }

    // Clear all stored request logs
    public void resetRequestLogs()
    {
        requestLogs.clear();
    }
}
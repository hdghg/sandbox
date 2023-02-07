package com.example.demohibernate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.util.concurrent.RateLimiter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE )
@Testcontainers
public class LoadTestTests {

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12.2");

    @Autowired
    private MockMvc mockMvc;

    private final AtomicLong iterations = new AtomicLong();

    final RateLimiter rateLimiter = RateLimiter.create(2000.0);

    @Test
    void test500Rps() throws Exception {
        System.out.println("Starting test...");
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);

        ExecutorService e = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        AtomicBoolean shouldContinue = new AtomicBoolean(true);
        for (int i = 0; i < 10; i++) {
            futures.add(e.submit(() -> testAsync(shouldContinue)));
        }

        long epoch = 0;
        long previousValue = 0;
        while (Instant.now().isBefore(end)) {
            Optional<Future<?>> done = futures.stream()
                    .filter(f -> f.isDone())
                    .findAny();
            if (done.isPresent()) {
                done.get().get();
            }
            if (Instant.now().getEpochSecond() != epoch) {
                System.out.println("Current rps: " + (iterations.longValue() - previousValue));
                previousValue = iterations.longValue();
                epoch = Instant.now().getEpochSecond();
            }
            TimeUnit.MILLISECONDS.sleep(250L);
        }
        shouldContinue.set(false);
        System.out.println("Total iterations: " + iterations.longValue());
        e.shutdown();
        e.awaitTermination(1, TimeUnit.SECONDS);
    }

    private Void testAsync(AtomicBoolean shouldContinue) throws Exception {
        MockHttpServletRequestBuilder request = get("/author/1");
        while (shouldContinue.get()) {
            if (rateLimiter.tryAcquire(1, TimeUnit.SECONDS)) {
                mockMvc.perform(request).andExpect(status().isOk());
                iterations.incrementAndGet();
            }
        }
        return null;
    }
}

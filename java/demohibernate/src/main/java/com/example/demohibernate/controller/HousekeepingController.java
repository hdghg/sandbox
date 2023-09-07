package com.example.demohibernate.controller;

import com.example.demohibernate.service.LongRunningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class HousekeepingController {

    private static final Logger log = LoggerFactory.getLogger(HousekeepingController.class);

    @Autowired
    private LongRunningService longRunningService;

    @GetMapping("/housekeeping")
    public void housekeeping(@RequestParam(value = "async", defaultValue = "true") boolean async)
            throws ExecutionException, InterruptedException {
        ListenableFuture<String> future = longRunningService.doSomethingAsync();
        if (async) {
            future.addCallback((t) -> log.info(t), (e) -> log.error("operation failed", e));
        } else {
            future.get();
        }
    }

}

package com.example.scaching.web;

import com.example.scaching.service.LenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private LenCacheService lenCacheService;

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name) {
        return "Hello, " + lenCacheService.len(name);
    }

    @GetMapping("/hello-put")
    public String helloPut(@RequestParam("name") String name) {
        return "Hello, " + lenCacheService.cput(name);
    }

    @GetMapping("/evict")
    public String evict(@RequestParam("name") String name) {
        lenCacheService.evict(name);
        return "Evicted";
    }
}

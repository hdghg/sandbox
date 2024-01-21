package com.example.scaching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class LenCacheService {

    private static final Logger log = LoggerFactory.getLogger(LenCacheService.class);

    @Cacheable("users")
    public Integer len(String s) {
        log.info("Called len()");
        return s.length();
    }

    @CachePut("users")
    public Integer cput(String s) {
        log.info("Called cput()");
        return s.length();
    }

    @CacheEvict(value = "users", key = "#s")
    public void evict(String s) {
        log.info("Called evict()");
    }
}

package com.github.hdghg.demo.service;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public Result prepareGreeting(@Nonnull String name) {
        if (name.startsWith("_")) {
            return new DemoService.UserIsNotWelcome();
        } else {
            return new DemoService.Success("Hello, " + name);
        }
    }
}

package com.github.hdghg.demo.service;

import javax.annotation.Nonnull;

public interface DemoService {

    Result prepareGreeting(@Nonnull String name);

    sealed interface Result {
    }

    record Success(@Nonnull String data) implements Result {
    }

    record UserIsNotWelcome() implements Result {
    }

}

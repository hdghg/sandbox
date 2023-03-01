package com.example.demo.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MyProcessor {

    String INPUT1 = "input1";

    @Input(INPUT1)
    SubscribableChannel input1();

}



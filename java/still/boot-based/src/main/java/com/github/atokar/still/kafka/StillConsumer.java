package com.github.atokar.still.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StillConsumer {

    @KafkaListener(topics = "boot-still", groupId = "group-1")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group group-1: " + message);
    }

}

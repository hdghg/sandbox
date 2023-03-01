package com.github.atokar.still.rest;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/produce")
    public CompletableFuture<String> produce(String key, String value) {
        ProducerRecord<String, String> pr = new ProducerRecord<>("boot-still", key, value);
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(pr);
        return send.completable()
                .thenApplyAsync(i -> "OK");
    }
}

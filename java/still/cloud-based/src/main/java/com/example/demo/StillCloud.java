package com.example.demo;

import com.example.demo.messaging.MyProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@SpringBootApplication
@EnableBinding(MyProcessor.class)
public class StillCloud {

    public static void main(String[] args) {
        SpringApplication.run(StillCloud.class, args);
    }

    @StreamListener(MyProcessor.INPUT1)
    public void processVote(String vote) {
        System.err.println(vote);
    }

}

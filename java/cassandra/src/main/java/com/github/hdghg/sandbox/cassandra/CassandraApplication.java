package com.github.hdghg.sandbox.cassandra;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import com.github.hdghg.sandbox.cassandra.service.VetClinicService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@SpringBootApplication
public class CassandraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CassandraApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(VetClinicService vetClinicService) {
        return args -> {
            Instant end = Instant.now().plusSeconds(5);
            while (Instant.now().isBefore(end) && !Thread.currentThread().isInterrupted()) {
                Vet vet = vetClinicService.createVet(new Vet(UUID.randomUUID(),
                        randomAlphabetic(20), randomAlphabetic(20), Collections.singleton(randomAlphabetic(20))));
//                    vetClinicService.assignVetToClinic(vet.getId(), randomAlphabetic(2).toUpperCase());
            }
        };

    }
}

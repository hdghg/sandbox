package com.github.hdghg.sandbox.cassandra;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import com.github.hdghg.sandbox.cassandra.service.VetClinicService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            Instant end = Instant.now().plusSeconds(3);
            while (Instant.now().isBefore(end)) {
                List<Vet> toPersist = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    toPersist.add(new Vet(UUID.randomUUID(), randomAlphabetic(20),
                            randomAlphabetic(20), Collections.singleton(randomAlphabetic(20))));
                }
                vetClinicService.createVets(toPersist);
//                    vetClinicService.assignVetToClinic(vet.getId(), randomAlphabetic(2).toUpperCase());
            }
            System.err.println("Done runner");
        };

    }
}

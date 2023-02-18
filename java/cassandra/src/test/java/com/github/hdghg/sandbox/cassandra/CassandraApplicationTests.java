package com.github.hdghg.sandbox.cassandra;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import com.github.hdghg.sandbox.cassandra.repository.VetRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@SpringBootTest
@ContextConfiguration(initializers = CassandraInitializer.class)
class CassandraApplicationTests {

    private final static Logger log = LoggerFactory.getLogger(CassandraApplicationTests.class);

    @Autowired
    private VetRepository vetRepository;

    @Test
    void contextLoads() {
        vetRepository.deleteAll();

        Vet john = new Vet(UUID.randomUUID(), "John", "Doe", new HashSet<>(Arrays.asList("surgery")));
        Vet jane = new Vet(UUID.randomUUID(), "Jane", "Doe", new HashSet<>(Arrays.asList("radiology", "surgery")));

        Vet savedJohn = vetRepository.save(john);
        Vet savedJane = vetRepository.save(jane);

        vetRepository.findAll().forEach(v -> log.info("Vet: {}", v));
        vetRepository.findById(savedJohn.getId()).ifPresent(v -> log.info("Vet by id: {}", v));

        log.info("Count: " + vetRepository.count());

    }

}

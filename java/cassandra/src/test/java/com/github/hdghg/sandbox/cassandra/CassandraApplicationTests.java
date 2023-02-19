package com.github.hdghg.sandbox.cassandra;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import com.github.hdghg.sandbox.cassandra.repository.VetByClinicRepository;
import com.github.hdghg.sandbox.cassandra.repository.VetRepository;
import com.github.hdghg.sandbox.cassandra.service.VetClinicService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.cassandra.core.mapping.BasicMapId.id;

@SpringBootTest
@ContextConfiguration(initializers = CassandraInitializer.class)
class CassandraApplicationTests {

    private final static Logger log = LoggerFactory.getLogger(CassandraApplicationTests.class);

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private VetByClinicRepository vetByClinicRepository;

    @Autowired
    private VetClinicService vetClinicService;

    @Test
    void clusteringEtc() {
        vetRepository.deleteAll();

        Vet john = new Vet(UUID.randomUUID(), "John", "Doe", new HashSet<>(Arrays.asList("surgery")));
        Vet jane = new Vet(UUID.randomUUID(), "Jane", "Doe", new HashSet<>(Arrays.asList("radiology", "surgery")));

        Vet savedJohn = vetRepository.save(john);
        Vet savedJane = vetRepository.save(jane);

        vetRepository.findAll().forEach(v -> log.info("Vet: {}", v));
        vetRepository.findById(savedJohn.getMapId()).ifPresent(v -> log.info("Vet by id: {}", v));

        log.info("Count: " + vetRepository.count());

    }

    @Test
    void verifyVetByClinic() {
        List<Vet> forCreation = new ArrayList<>();
        forCreation.add(new Vet(UUID.randomUUID(), "Bill", "Gates", Collections.singleton("it")));
        forCreation.add(new Vet(UUID.randomUUID(), "Alice", "Cooper", new HashSet<>(Arrays.asList("lab", "sing"))));
        forCreation.add(new Vet(UUID.randomUUID(), "Steve", "Jobs", Collections.singleton("entertainment")));
        forCreation.add(new Vet(UUID.randomUUID(), "Mark", "Zuckerberg", Collections.singleton("security")));

        vetClinicService.createVets(forCreation);

        vetClinicService.assignVetToClinic(forCreation.get(0).getId(), "DrSlon");
        vetClinicService.assignVetToClinic(forCreation.get(1).getId(), "DrSlon");
        vetClinicService.assignVetToClinic(forCreation.get(2).getId(), "DrSlon");
        vetClinicService.assignVetToClinic(forCreation.get(3).getId(), "PawPaw");

        List<MapId> mapIds = Arrays.asList(id("clinicName", "PawPaw"), id("clinicName", "DrSlon"), id("clinicName", "Aaaa"));
        vetByClinicRepository.findAllById(mapIds).forEach(c -> {
            vetRepository.findById(id("id", c.getVetId())).ifPresent(v ->
                    System.err.println("" + c.getClinicName() + " " + c.getFullName() + " " + v.getSpecialties()));
        });
        System.err.println(vetByClinicRepository.countByClinicName("DrSlon"));

    }

}

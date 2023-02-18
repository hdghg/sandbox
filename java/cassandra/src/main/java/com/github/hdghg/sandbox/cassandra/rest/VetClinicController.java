package com.github.hdghg.sandbox.cassandra.rest;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import com.github.hdghg.sandbox.cassandra.entity.VetByClinic;
import com.github.hdghg.sandbox.cassandra.repository.VetByClinicRepository;
import com.github.hdghg.sandbox.cassandra.repository.VetRepository;
import com.github.hdghg.sandbox.cassandra.service.VetClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/vetClinic")
public class VetClinicController {

    @Autowired
    private VetClinicService vetClinicService;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private VetByClinicRepository vetByClinicRepository;

    @GetMapping("/vet/{id}")
    public Optional<Vet> vetById(@PathVariable("id") UUID id) {
        return vetRepository.findById(id);
    }

    @GetMapping("/clinic/{name}")
    public Iterable<VetByClinic> vetsByClinic(@PathVariable("name") String name) {
        return vetByClinicRepository.findAllById(Collections.singleton("name"));
    }

    @GetMapping("/clinic")
    public Iterable<VetByClinic> vetsByClinic() {
        return vetByClinicRepository.findAll();
    }

    @PostMapping("/create")
    void addVet(@RequestParam String clinicName, @RequestParam String firstName, @RequestParam String lastName,
                @RequestParam Set<String> specSet) {
        Vet vet = vetClinicService.createVet(new Vet(UUID.randomUUID(), firstName, lastName, specSet));
        vetClinicService.assignVetToClinic(vet.getId(), clinicName);
    }

}

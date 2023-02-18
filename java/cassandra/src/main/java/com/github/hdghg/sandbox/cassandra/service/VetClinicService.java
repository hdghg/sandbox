package com.github.hdghg.sandbox.cassandra.service;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import com.github.hdghg.sandbox.cassandra.entity.VetByClinic;
import com.github.hdghg.sandbox.cassandra.repository.VetByClinicRepository;
import com.github.hdghg.sandbox.cassandra.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VetClinicService {

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private VetByClinicRepository vetByClinicRepository;

    public Vet createVet(Vet vet) {
        return vetRepository.save(vet);
    }

    public void assignVetToClinic(UUID vetId, String clinicName) {
        vetRepository.findById(vetId).ifPresent(v -> vetByClinicRepository.save(
                new VetByClinic(clinicName, v.getId(), v.getFirstName() + " " + v.getLastName())));
    }
}

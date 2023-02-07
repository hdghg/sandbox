package com.github.hdghg.sandbox.cassandra.repository;

import java.util.UUID;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import org.springframework.data.repository.CrudRepository;

public interface VetRepository extends CrudRepository<Vet, UUID> {
    Vet findByFirstName(String username);
}

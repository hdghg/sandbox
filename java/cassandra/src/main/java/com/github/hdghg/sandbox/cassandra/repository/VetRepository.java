package com.github.hdghg.sandbox.cassandra.repository;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface VetRepository extends CrudRepository<Vet, UUID> {
}

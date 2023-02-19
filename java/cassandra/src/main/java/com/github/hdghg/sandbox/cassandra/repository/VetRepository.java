package com.github.hdghg.sandbox.cassandra.repository;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface VetRepository extends CassandraRepository<Vet, UUID> {
}

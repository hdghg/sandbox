package com.github.hdghg.sandbox.cassandra.repository;

import com.github.hdghg.sandbox.cassandra.entity.Vet;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

public interface VetRepository extends MapIdCassandraRepository<Vet> {
}

package com.github.hdghg.sandbox.cassandra.repository;

import com.github.hdghg.sandbox.cassandra.entity.VetByClinic;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

public interface VetByClinicRepository extends MapIdCassandraRepository<VetByClinic> {

    int countByClinicName(String clinicName);

}

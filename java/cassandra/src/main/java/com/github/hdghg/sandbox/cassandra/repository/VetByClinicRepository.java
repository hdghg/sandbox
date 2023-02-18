package com.github.hdghg.sandbox.cassandra.repository;

import com.github.hdghg.sandbox.cassandra.entity.VetByClinic;
import org.springframework.data.repository.CrudRepository;

public interface VetByClinicRepository extends CrudRepository<VetByClinic, String> {

    int countByClinicName(String clinicName);

}

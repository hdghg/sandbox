package com.github.hdghg.sandbox.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("vet_by_clinic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VetByClinic {

    @PrimaryKey
    private String clinicName;

    private UUID vetId;

    private String fullName;

}

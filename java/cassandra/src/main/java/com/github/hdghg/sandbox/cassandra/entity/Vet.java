package com.github.hdghg.sandbox.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vet {

    @PrimaryKey
    private UUID id;

    private String firstName;

    private String lastName;

    private Set<String> specialties;

}

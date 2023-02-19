package com.github.hdghg.sandbox.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.core.mapping.MapIdentifiable;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("vet_by_clinic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VetByClinic implements MapIdentifiable {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String clinicName;

    private UUID vetId;

    @PrimaryKeyColumn
    private String fullName;

    @Override
    public MapId getMapId() {
        return BasicMapId.id()
                .with("clinicName", clinicName)
                .with("fullName", fullName);
    }
}

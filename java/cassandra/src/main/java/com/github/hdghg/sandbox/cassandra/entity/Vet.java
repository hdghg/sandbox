package com.github.hdghg.sandbox.cassandra.entity;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Vet {

    @PrimaryKey
    private UUID id;

    private String firstName;

    private String lastName;

    private Set<String> specialties;

    public Vet(UUID id, String firstName, String lastName, Set<String> specialties) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialties = specialties;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<String> specialties) {
        this.specialties = specialties;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
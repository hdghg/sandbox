package com.example.demohibernate.repository;

import com.example.demohibernate.model.entity.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitRepository extends JpaRepository<Fruit, Long> {

    Fruit findByFruitId(Long fruitId);
}

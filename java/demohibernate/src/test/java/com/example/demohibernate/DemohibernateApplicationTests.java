package com.example.demohibernate;

import com.example.demohibernate.model.entity.Fruit;
import com.example.demohibernate.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class DemohibernateApplicationTests {

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12.2");

    @Autowired
    private FruitRepository fruitRepository;

    @Test
    void fruitNullableColumn() {
        Fruit fruit = new Fruit();
        fruit.setColor("red");
        fruit.setName("tomato");
        Fruit save = fruitRepository.save(fruit);

        assertEquals("red", save.getColor());
        assertEquals("tomato", save.getName());

        Fruit reference = fruitRepository.findByFruitId(save.getFruitId());
        assertEquals("red", reference.getColor());
        assertEquals("tomato", reference.getName());

    }

    @Test
    void fruitDefaultInNullableColumn() {
        Fruit fruit = new Fruit();
        fruit.setName("apple");
        Fruit save = fruitRepository.save(fruit);

        assertEquals("white", save.getColor());
        assertEquals("apple", save.getName());

        Fruit reference = fruitRepository.findByFruitId(save.getFruitId());
        assertEquals("white", reference.getColor());
        assertEquals("apple", reference.getName());
    }

}

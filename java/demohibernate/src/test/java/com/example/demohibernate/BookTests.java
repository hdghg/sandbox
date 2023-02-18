package com.example.demohibernate;

import com.example.demohibernate.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Testcontainers
class BookTests {

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12.2");

    @Autowired
    private BookRepository bookRepository;


    @Test
    @Transactional
    void testDelete() {
        bookRepository.deleteByTitleIn(List.of("title4", "title5"));
        bookRepository.findAll().stream()
                .forEach(a -> System.out.println(a.getTitle()));
    }

}

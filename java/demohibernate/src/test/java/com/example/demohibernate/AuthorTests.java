package com.example.demohibernate;

import com.example.demohibernate.model.entity.Author;
import com.example.demohibernate.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Testcontainers
class AuthorTests {

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12.2");

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @Transactional
    void filter() {
        List<Author> authorList = authorRepository.filter("John Wick", "titl", PageRequest.of(1, 2));

        authorList.forEach(i -> System.err.println(i));
    }

    @Test
    @Transactional
    void filterIds() {
        List<Long> ids = authorRepository.findIds("John Wick", "titl", PageRequest.of(1, 2));
        List<Author> authorList = authorRepository.findAllById(ids);

        authorList.forEach(i -> System.err.println(i));
    }



}

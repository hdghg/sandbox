package com.example.demohibernate.controller;

import com.example.demohibernate.model.dto.AuthorDto;
import com.example.demohibernate.model.entity.Author;
import com.example.demohibernate.model.entity.Book;
import com.example.demohibernate.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/author/{id}")
    public AuthorDto byId(@PathVariable("id") Long id) {
        Author author = authorRepository.getReferenceById(id);
        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(author.getAuthorId());
        authorDto.setName(author.getName());
        List<String> bookList = author.getBookList().stream()
                .map(Book::getTitle)
                .toList();
        authorDto.setBookList(bookList);
        return authorDto;
    }
}

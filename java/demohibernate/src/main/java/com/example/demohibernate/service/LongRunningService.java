package com.example.demohibernate.service;

import com.example.demohibernate.model.entity.Author;
import com.example.demohibernate.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LongRunningService {

    private static final Logger log = LoggerFactory.getLogger(LongRunningService.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    @Async
    public ListenableFuture<String> doSomethingAsync() {
        try {
            List<Author> findAll = authorRepository.findAll();
            for (int i = 0; i < findAll.size(); i++) {
                Author author = findAll.get(i);
                log.info("Processing author {}...", author.getName());
                author.setName(author.getName() + "___");
                TimeUnit.SECONDS.sleep(5);
                if (i == 1) {
                    throw new IllegalStateException("");
                }
                log.info("Complete author {}", author.getName());
            }
            log.info("Complete operation");
            return new AsyncResult<>("12313");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

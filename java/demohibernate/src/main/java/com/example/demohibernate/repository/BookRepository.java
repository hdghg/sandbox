package com.example.demohibernate.repository;

import com.example.demohibernate.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query(value = "DELETE FROM book WHERE title in :collection", nativeQuery = true)
    void deleteByTitleIn(@Param("collection") Collection<String> collection);

}

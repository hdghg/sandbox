package com.example.demohibernate.repository;

import com.example.demohibernate.model.entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Cannot use JOIN FETCH here because hibernate won't be able to
     * place limit and offset
     */
    @Query("""
            SELECT a FROM Author a
            LEFT JOIN a.bookList b
            WHERE (:name is null OR a.name LIKE %:name%)
            AND (:title is null OR b.title LIKE :title)
            """)
    List<Author> filter(@Param("name") String name,
                        @Param("title") String title,
                        Pageable pageable);

    /**
     * Step 1. Load ids as list
     * Step 2. Load entities by calling {@link org.springframework.data.repository.CrudRepository#findAllById(Iterable)}
     * Step 3. Sort by ids from step 1
     */
    @Query("""
            SELECT a.authorId FROM Author a
            LEFT JOIN a.bookList b
            WHERE (:name is null OR a.name LIKE %:name%)
            AND (:title is null OR b.title LIKE :title)
            """)
    List<Long> findIds(@Param("name") String name,
                       @Param("title") String title,
                       Pageable pageable);
}

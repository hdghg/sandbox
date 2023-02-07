package com.mycompany.gb.dao;

import com.mycompany.gb.model.domain.Comment;

import java.util.List;

public interface CommentDao {
    List<Comment> findAll();

    Comment findOne(long id);

    void delete(long id);

    Comment save(Comment comment);
}
